# GetOuttaHere

# How does it work?

When player with 'getouttahere.stay' permission joins the server, random player without this permission will get kicked out, with KickMessage being displayed.
If no such player exists, player joining will get NoKickMessage

# Config

KickMessage: "&3&lYou have been kicked because the server has too many players"

NoKickMessage: "&4&lSorry, no players left to kick"

IgnoreFull: false

if IgnoreFull is set to true, no players will get kicked when player joins the server, bypassing the server player limit (for example: there can be 11/10 players online)

# Commands

/getouttahere reload

reloads the config, you need 'getouttahere.admin' permission to use it

# Permissions

getouttahere.stay
getouttahere.admin
