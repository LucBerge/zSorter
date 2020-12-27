# zSorter

The zSorter plugin allows you to manager sorting system in game. It is an alternate solution to standard slow and laggy systems.

## How it works

A sorter is a sorting system composed of multiple inputs and outputs (chests, hoppers...). Inputs and outputs are ordered by priority from 1 to infinity and outputs can handle multiple items.
An output can also be defined as an overflow.

The algorithm starts by looking for an item in the inputs. The input of priority 1 will be the first to be empty, then the 2...
If an item is found, the algorithm will list all the possible outputs and will try to put the item in the output. If the output of priority 1 is full, it will look for the output of priority 2, then 3...

## Commands

List all the existing sorters :
```
/sorter list
```

Create a sorter:
```
/sorter create <name> <description...>
```

Update a sorter description:
```
/sorter update <name> <new-description...>
```

Change a sorter speed (by default set to 1):
```
/sorter speed <name> <speed>
```

Delete a sorter:
```
/sorter delete <name>
```

Display a sorter informations (By default OUTPUTS mode):
```
/sorter info <name> <mode>
```

Enable/Disable a sorter:
```
/sorter toggle <name>
```

Define the target block as an input:
```
/sorter set_input <name> <priority>
```

Define the target block as an output (no items to define it as an overflow):
```
/sorter set_output <name> <priority> <items...>
```

Remove the target block from inputs:
```
/sorter remove_input <name>
```

Remove the target block from outputs :
```
/sorter remove_output <name>
```

Highlight all the inputs and outputs of the sorter during 1 minute. The single outputs can also be define by left clicking on a holder.  
```
/sorter magic <name>
```

## Permissions

The following permission allows you to use all the above commands.
```
zsorter.admin
```
