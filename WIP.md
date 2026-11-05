# WIP branch — what is here and where it goes

Personal fork of `glidiandev/kido`. Nothing on this branch has been sent upstream; only
`fix/philips-eum-hidden-and-emblem-comment` (PR #8) has.

Base: upstream `main`. Built and tested on a Philips 50PUS7334/12 (TP Vision PH9M_EA_5599, Android TV 9,
SDK 28) — the set this work started on.

## Commit map

| Commit | Touches | Issue | Upstreamable? |
|---|---|---|---|
| `bloom: let a halo fade out inside its view` | `GlowView.java` | #5 (first half) | Yes, but discuss the tuning first |
| `honeycomb: draw each app's emblem on the hex` | `HexView.java` | #7 / #3 | It is a feature; upstream calls panels-only deliberate |
| `footer selection, bloom wiring, channel recall` | `KidoActivity.java` | #4, #5 (wiring), feature | Mixed — see below |

The third commit carries three unrelated changes only because they share a file. Every edited region is
marked `LOCAL FORK:` in the source, so `git show 002979a -- app/src/main/java/dev/glidian/kido/KidoActivity.java`
plus those markers is enough to split it later:

- `focusLabel` field + `showStatus()` + `renderTicker()` → **#4**
- `glowBehind()` pad 88 and `glowBg.setGradientRadius(Kido.px(460))` → **#5**
- `chanBuf`/`handleChannelKey()`/`showChannelTyped()`/`tuneChannel()` and the `chanBuf` guards in
  `renderTicker()` → **channel recall**, a feature I added for this TV, not requested upstream

## Not on this branch

Device-specific package hiding (`org.droidtv.playtv`, `org.droidtv.channels` in `Kido.HIDDEN` for a
streaming-only set with no aerial) lives on `local/philips-50pus7334`. Do not put it in a PR — it would
hide Live TV for everyone.

## Numbers worth keeping

- Focused-tile glow: `GlowView(radius = px(44))`, padded `px(88)` = 2x radius, `setSpread(1f)`.
- Plate halo: left at `pad = radius`, `spread = 0.5` — the look Paul approved.
- Plate radial: `px(460)` inside a 1300x1000 view (half-height 500).

## Build

`tools/docker-build.sh` from cleanyourtv, or any JDK 17 + SDK 34:

```bash
docker run --rm -v $PWD:/repo -v cleanyourtv-gradle:/gradle -w /repo cleanyourtv-android-build \
  sh ./gradlew --no-daemon --console=plain assembleRelease
```
