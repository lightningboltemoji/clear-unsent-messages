# Clear Unsent Messages

[![](https://img.shields.io/endpoint?url=https://api.runelite.net/pluginhub/shields/installs/plugin/clear-unsent-messages)](https://runelite.net/plugin-hub/show/clear-unsent-messages)
[![](https://img.shields.io/endpoint?url=https://api.runelite.net/pluginhub/shields/rank/plugin/clear-unsent-messages)](https://runelite.net/plugin-hub/show/clear-unsent-messages)

A RuneLite plugin for clearing unsent messages from your chatbox

<img width="313" alt="Picture of a message with leading 1s" src="https://user-images.githubusercontent.com/51724788/208325355-3851a5fc-f6bf-47a0-a4b6-3aa14a7c8144.png">

<img width="313" alt="Video of the plugin clearing an unsent message from the chatbox" src="/video.webp"/>

## Settings

<img width="241" alt="Picture of the settings panel" src="https://github.com/user-attachments/assets/0adf980c-53af-4cde-b1b1-d7b73aa89899"/>

### Time-based

One method of clearing messages is based on **typing inactivity**, wherein we detect once a configurable duration has passed since the last key press. This can be set to only trigger for messages made up of spaces and numbers -- for example, " 11111" but not "sit".

_Enabled by default with a delay of 2500ms (2.5 seconds) for all messages_

### Hotkey (new!)

Another method of clearing messages is a **configurable hotkey**. When the hotkey is pressed, any unsent message is cleared, saving you from needing to hold down backspace.

_Disabled by default_

## Changelog

* 2024-10-10: Add instant clear hotkey. Refactor to support ignoring non-character keys (e.g. arrows).
* 2024-10-08: Fix compatibility with latest RuneLite version.
* 2023-09-06: Performance optimizations (do less on client thread).
* 2023-02-20: Fix compatibility with the Key Remapping plugin.
* 2023-02-09: Fix resizable mode. Fix text alignment when clearing long messages.
