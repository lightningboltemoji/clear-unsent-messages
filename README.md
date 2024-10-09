# Clear Unsent Messages

[![](https://img.shields.io/endpoint?url=https://api.runelite.net/pluginhub/shields/installs/plugin/clear-unsent-messages)](https://runelite.net/plugin-hub/show/clear-unsent-messages)
[![](https://img.shields.io/endpoint?url=https://api.runelite.net/pluginhub/shields/rank/plugin/clear-unsent-messages)](https://runelite.net/plugin-hub/show/clear-unsent-messages)

A RuneLite plugin for clearing unsent messages from your chatbox.

<img width="313" alt="Picture of a message with leading 1s" src="https://user-images.githubusercontent.com/51724788/208325355-3851a5fc-f6bf-47a0-a4b6-3aa14a7c8144.png">

<img width="178" src="https://user-images.githubusercontent.com/51724788/208325383-d0f0b999-5318-4dc8-8332-b1f6760ae375.gif" alt="Video of the plugin clearing an unsent message from the chatbox"/>

## Configuration

|Name|Purpose|Default value|
|-|-|-|
|Delay|After this period, any unsent message will cleared|1500ms|
|Only spaces/numbers|If checked, only clears messages made up of spaces and numbers. Useful if you want to pre-type messages (for example: "sit" while in a fight).|false|

## Updates

* 2023-09-06: Performance optimizations (do less on client thread).
* 2023-02-20: Fix compatibility with the Key Remapping plugin.
* 2023-02-09: Fix resizable mode. Fix text alignment when clearing long messages.
* 2024-10-08: Fix compatibility with latest RuneLite version.