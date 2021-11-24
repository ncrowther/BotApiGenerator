# Bot Generator

## Description

This utility runs a chat bot in RPA Studio to generate:
 - Wal 
 - Yaml 

## Prerequisites

IBM RPA V20.x or later
Java runtime  15 or later

## Usage

Download this repo as a zip and extract to a folder of your choice.

- In RPA Studio, open BotApiGenerator/bot/BuildABot.wal
- Run this script WITHOUT DEBUG (CTRL + F5).  If you run with debug the generated script will be malformed
- When the chat bot appears, select N to generate the bot from the chat bot, and then select the chat bot buttons to add bot parameters.
- When done select generate.  Avoid using the mouse or keyboard until the bot has finished generation.
- Once finished, you should see the generated wal in Studio, and a browser containing YAML to invoke the bot.
- Manually deploy the bot to your tenant and then use the YAML editor to test your Bot.
See https://youtu.be/v6xn30RtY1E

## Bugs and Limitations

Bot must be run without debug.  Only String parameters types are supported

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.
