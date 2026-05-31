# Hexapod

A simulated six-legged robot you can walk around procedurally-generated terrain. Inverse-kinematic leg control, tripod gait, body roll/pitch stabilisation against the ground slope. Keyboard, mouse, and DualSense/DualShock controller input.

![screenshot placeholder — drop a PNG/GIF in docs/ and replace this line](docs/screenshot.png)

## Run it

```bash
git clone <repo-url>
cd Hexapod/hexapod
mvn compile      # also extracts JOGL / JInput native libraries
mvn exec:exec    # launches org.pdeboer.hexapod.app.App
```

Requires Java 17+ and Maven. The JOGL / JInput native libraries for macOS, Linux, and Windows are unpacked into `target/natives/` by `mvn compile`.

> **Known broken (May 2026): macOS 26 + Apple Silicon.** Processing 4's P3D renderer (via JOGL 2.5.0) crashes at startup with `EXC_BREAKPOINT` / "Must only be used from the main thread", from both Maven and IntelliJ. Likely a Metal/OpenGL bridge regression in macOS 26 that JOGL doesn't tolerate. Last known good: macOS 14 on Intel hardware. Linux and Windows are untested in this run-through but expected to be fine.

## Controls

### Mouse

| Action | Effect |
| --- | --- |
| Drag | Orbit the camera |
| Shift + drag | Drag selected leg's foot in the xy-plane |
| Alt + drag | Tilt body roll / pitch |

### Keyboard

| Keys | Action |
| --- | --- |
| `↑` `↓` | Accelerate forward / backward |
| `←` `→` | Rotate left / right |
| `.` | Stop |
| `0` | Reset pose |
| `1`–`6` | Select leg (for shift+drag) |
| `w` `s` `a` `d` | Translate body forward / back / left / right |
| `q` `e` | Translate body up / down |
| `j` `l` | Roll − / + |
| `i` `k` | Yaw + / − |
| `u` `o` | Pitch − / + |

### DualSense / DualShock 4 controller

| Input | Effect |
| --- | --- |
| Left stick | Speed (Y axis) and yaw rotation (X axis) |
| Right stick | Orbit the camera |
| D-pad | One-shot move forward / right / back / left |

## Project layout

- `hexapod/src/main/java/org/pdeboer/hexapod/` — robot model: `Hexapod`, `Leg`, gait, IK, stabilisation
- `hexapod/src/main/java/org/pdeboer/hexapod/app/` — Processing-based simulation app (the thing you run)
- `hexapod/src/main/java/org/pdeboer/util/` — vector / quaternion math, Perlin noise
- `hexapod/src/main/java/org/pdeboer/genetic/` — genetic-algorithm experiments (gait evolution, leg loading, …). **Incomplete; not part of the showcase.** Kept around as scaffolding.

## Status

Hobby project, on-and-off since 2011. Active development has moved to a successor project; this repository is no longer being extended, but the simulator runs and is fun to drive around.

## License

Apache License 2.0 — see [LICENSE](LICENSE) and [NOTICE](NOTICE).
