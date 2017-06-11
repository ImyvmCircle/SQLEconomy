# SQLEconomy
forked from https://gitlab.com/andrewward2001/SQLEconomy
SQLEconomy is a bukkit plugin that combines MySQL and SQLite and puts them into an economy plugin. The great thing about SQL is it's extremely organized. Each table is like a spreadsheet, keeping everything organized. If yo don't have and experience with SQL, SQLEconomy doesn't care! It will create a table for you, all on its own.

## Installation
Grab the latest release, then head over to [dev.bukkit.org](https://dev.bukkit.org/projects/sqleconomee) for the instructions.

## Permissions
This is a copy/paste from plugin.yml:
```
sqleconomy.money:
  description: Give access /money command
  default: true
sqleconomy.money.transfer:
  description: Gives access to /money transfer
  default: true
sqleconomy.money.*:
  description: Gives access to all SQLEconomy /money commands
  children:
    sqleconomy.money.give: true
    sqleconomy.money.remove: true
    sqleconomy.money.seeothers: true
sqleconomy.money.give:
  description: Give other players money independent from yours
  default: op
sqleconomy.money.remove:
  description: Take money from other players
  default: op
sqleconomy.money.seeothers:
  description: See how much money someone else has
  default: op
sqleconomy.config:
  description: Configure SQLEconomy
  default: op
```
To Do List
* change money type to double
* hide meaasage outputed on console
