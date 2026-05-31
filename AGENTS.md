# AGENTS.md

This file provides guidance to AI coding agents working in this repository.

## Build & test

Maven project (Java 17). All commands run from the `hexapod/` subdirectory.

```bash
cd hexapod
mvn compile                                # build + unpack JOGL/JInput natives into target/natives/
mvn test                                   # run all tests
mvn test -Dtest=HexapodTest                # single test class
mvn test -Dtest=HexapodTest#test_init      # single test method
mvn exec:exec                              # run main simulation (org.pdeboer.hexapod.app.App)
```

JUnit 5 + Mockito are used for tests. The `maven-nativedependencies-plugin` is bound to `generate-resources` and unpacks JOGL/JInput natives; `exec-maven-plugin` is preconfigured to launch the main `App` with the right `-Djava.library.path`.

Runtime status: the sim currently crashes at startup on macOS 26 + Apple Silicon (JOGL 2.5.0 / Metal-OpenGL bridge issue). See `README.md`.

## Entry points

There are multiple `App` classes, each a Processing `PApplet` with its own `main`. Pick the one matching the task:

- `org.pdeboer.hexapod.app.App` — main hexapod simulation (terrain + robot + mouse/keyboard/DualSense controller).
- `org.pdeboer.genetic.app.gait.App` — evolve a hexapod gait via GA.
- `org.pdeboer.genetic.app.gait2.App` — hand-authored gait playback (no GA).
- `org.pdeboer.genetic.app.leg.App` — evolve a 2D leg under load.
- `org.pdeboer.genetic.app.line.App` — toy GA fitting a line.

## Architecture

### Robot model (`org.pdeboer.hexapod`)

`Hexapod` owns six `Leg`s and a `Terrain`. Body state is `center` (`Vector3d`) + `rotation[ROLL, PITCH, YAW]`. Body-relative leg attachment points live in a static `offset[]` array; the world-space attachment `p1` is computed as `center + rotationMatrix * offset[i]`.

Each `Leg` has four points (`p1..p4`: hip → coxa-end → femur-end → foot) and three joint angles (`ra, rb, rc`) bounded by per-axis `angleRange`. `Leg.update(bodyRotation)` is forward kinematics (angles → points). `Leg.updateInverse(rotation)` is **stochastic** IK — random perturbations with simulated-annealing-style temperature schedule, minimizing distance from current `p4` to target `p4`. This is intentional: it tolerates unreachable targets gracefully.

`Hexapod.updateInverse()` is the standard "I changed something, recompute everything" routine: `updateP1()` → parallel `leg.updateInverse(rotation)` → `updateForward()`. Call it after mutating body pose or any leg target.

### Update loop

`Hexapod.update()` is called once per frame from `App.draw()`. It picks `STOP` vs `MOVE` based on `speed`/`rotationSpeed`:

- **idle**: ease each foot toward terrain height so legs settle on uneven ground.
- **move**: `stabilise()` aligns body roll/pitch with terrain normal and clamps height; then `updateMove()` runs the gait. `tripodGait` is the active gait (`waveGait` / `rippleGait` defined but unused). Per step: legs in the active gait phase get `startMoving(dst)` where `dst` extrapolates `speedV()` (yaw-rotated body-frame speed) over `STEP_COUNT` frames, plus a yaw-rotation displacement. Stepping legs lerp through an arched mid-point and stop when `touchGround()`.

`LegConfig` evaluates which 3-leg subset forms a stable support — used by `calculateLegConfig()` for display.

### Math (`org.pdeboer.util`)

`Vector3d`, `Vector2d` are immutable value types with `add*`, `sub*`, `multiply`, `lerp`, etc. Prefer these over raw arrays.

`Rotation3D` is a quaternion-backed rotation built from `(roll, pitch, yaw)` via `Rotation3D.of(...)`. Multiplication order is `qx · qy · qz`. Note the codebase mixes conventions: `Hexapod.rotationMatrix()` negates all three angles when building the matrix because `rotation[]` stores anti-clockwise angles but `Rotation3D` is clockwise.

### Terrain

`Terrain` is a functional interface: `(x, y) -> height`. `TerrainImpl` is a Perlin-noise heightmap scaled by `noiseScale`. The default `angle(x, y)` returns `[roll, pitch]` from finite differences and is what `stabilise()` uses.

### Genetic framework (`org.pdeboer.genetic`)

`model/` defines interfaces (`IPhenotype`, `IChromosome`, `ICrossover`, `IMutate`, `IPhenotypeFactory`, `IPopulation`, `IAlgorithm`, `ICondition`). `algorithm/` implements them. `Algorithm.start()` spawns one `Thread` per `Population`, each looping `generate()` until `stop()`. Selection is fitness-proportional via `Arrays.binarySearch` on a cumulative fitness array; the top `countElite=2` survive unchanged each generation. Experiments under `app/<name>/` plug their own phenotype + factory + mutator into this scaffold.

## Conventions

- Angles in radians internally; `App` prints them in degrees via `fmtAngle`.
- `ROLL`, `PITCH`, `YAW` index constants are defined on `Hexapod` (0, 1, 2) — use them when indexing `rotation[]`.
- `Leg.p1..p4` are public mutable fields, written from both inside `Leg` and outside (e.g. `App.mouseDragged` directly sets `leg.p4` for drag-to-pose). Treat them as the shared geometry state; call `updateInverse` after mutating.
- Per-leg angle limits are set in the `Hexapod` constructor in degrees via `setAngelRange` (note the `Angel` spelling) and converted to radians internally.
