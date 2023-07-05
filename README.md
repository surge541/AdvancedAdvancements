# Advanced Advancements
A mod that improves the visuals of advancements.

This entire mod has been inspired by [Advancement Plaques](https://github.com/AHilyard/AdvancementPlaques), which
hadn't been updated to 1.20.1 at the time of writing this. This does use the assets from Advancement Plaques,
so full credit goes to AHilyard for the creation of them.

This mod utilises [NanoVG](https://github.com/memononen/nanovg) for rendering.

Any pull requests to solve the atrocity that is this code, or just to add some new features, are very, very welcome!

### Configuration
If you look in `.minecraft/config` after running the mod for the first time, you should see a file called
`advanced-advancements.json`. This holds all customisability for the mod.

| Entry           | Description                                                                         | Allowed Values                                                                                                                        | Default       |
|-----------------|-------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------|---------------|
| ChallengeColour | The colour of the title text for a challenge toast.                                 | A hex code, starting with `#`.                                                                                                        | `#AA00AA`     |
| FadeOutEasing   | The easing to apply for the animation when a toast is fading out.                   | Any easing present [here](https://github.com/Wolfsurge/JavaAnimationSystem/blob/master/src/main/java/me/surge/animation/Easing.java). | `EXPO_IN_OUT` |
| RecipesColour   | The colour of the title text for a recipes toast.                                   | A hex code, starting with '#'.                                                                                                        | `#FFA500`     |
| MessageColour   | The colour of the messages for any toast.                                           | A hex code, starting with '#'.                                                                                                        | `#808080`     |
| Scale           | The scale of how big messages should be.                                            | A value greater than 0.                                                                                                               | `1`           |
| YOffset         | How far down from the top of the screen the toast is.                               | A value greater or equal to 0.                                                                                                        | `50`          |
| EntryAnimation  | How the toasts should become visible.                                               | `SCALE`, `SLIDE` or `FLASH`                                                                                                           | `FLASH`       |
| FadeInEasing    | The easing to apply for the animation when a toast is fading in.                    | Any easing present [here](https://github.com/Wolfsurge/JavaAnimationSystem/blob/master/src/main/java/me/surge/animation/Easing.java). | `EXPO_IN_OUT` |
| FadeIn          | How long the fade in animation is, in milliseconds.                                 | An integer value, above 0.                                                                                                            | `1000`        |
| FadeOut         | How long the fade out animation is, in milliseconds.                                | An integer value, above 0.                                                                                                            | `1500`        |
| Recipes         | Whether to override toasts for recipes.                                             | `true` or `false`.                                                                                                                    | `true`        |
| Hold            | How long to hold the animations visibility, after it has faded in, in milliseconds. | An integer value, above 0.                                                                                                            | `2000`        |
| TaskColour      | The colour of the title text for a task toast.                                      | A hex code, starting with `#`.                                                                                                        | `#55FF55`     |
| Mute            | Whether to mute the sounds made when a toast is displayed.                          | `true` or `false`.                                                                                                                    | `false`       |
| GoalColour      | The colour of the title text for a goal toast.                                      | A hex code, starting with `#`.                                                                                                        | `#55FF55`     |
| Advancements    | Whether to override toasts for advancements.                                        | `true` or `false`.                                                                                                                    | `true`        |