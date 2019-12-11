### Keymap

Keymap is the number of mappings hardware keys on mnemonic names (symbols).

#### Keymap example:

```groovy
['§' : '`', '1': '1', '2': '2', '3': '3', '4': '4', '5': '5', '6': '6', '7': '7', '8': '8', '9': '9', '0': '0', '-': '-', '=': '=', '⌫': 'remove_previous_symbol',
'⇥' : '', 'q': 'q', 'w': 'w', 'e': 'e', 'r': 'r', 't': 't', 'y': 'y', 'u': 'u', 'i': 'i', 'o': 'o', 'p': 'p', '[': '[', ']': ']', '↩': '',
'⌘' : '', 'a': 'a', 's': 's', 'd': 'd', 'f': 'f', 'g': 'g', 'h': 'h', 'j': 'j', 'k': 'k', 'l': 'l', ';': ';', '\'': '\'', '\\': '\\',
'⇧' : '', '⎋': '', 'z': 'z', 'x': 'x', 'c': 'c', 'v': 'v', 'b': 'b', 'n': 'n', 'm': 'm', ',': ',', '.': '.', '/': '/',
'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']
```

### Layout

Layout contains keymap and describes how mnemonic names (symbols) converted to combination
 of real keys to get it. Layout below has mnemonic name `copy`, it will be translated
 to combination `command+c` 

#### Layout example

```groovy
[keymap : ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
           '⇥' : 'switch_apps', 'q': 'quit', 'w': 'close', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': 'open', 'p': 'print', '[': '', ']': '', '↩': '',
           '⌘' : '', 'a': 'select_all', 's': 'save', 'd': '', 'f': 'search', 'g': 'search_forward', 'h': 'hide', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
           '⇧' : '', '⎋': '', 'z': 'undo', 'x': 'cut', 'c': 'copy', 'v': 'paste', 'b': '', 'n': 'new', 'm': 'minimize', ',': 'preferences', '.': '', '/': '',
           'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': 'spotlight', '←': 'home', '↑': 'begin_document', '↓': 'end_document', '→': 'end'],
 symbols: { [cut: [key_code: 'c', modifiers: ['left_control'] ] ] }]
```

As you see above, `symbols` is a function which returns a `Map` with key-values structure:

```groovy
[(symbol): [key_code: 'key', modifiers: [ 'list of modifiers' ]],
 (symbol): [key_code: 'key', modifiers: [ 'list of modifiers' ]]]
```

Also a `symbols` function gets keymap as a first argument.

Concept of `symbol` it is number of action which user should do to get symbol or
 Karabiner should do to produce it. 
 
### Symbol

Symbol is an number of keys which should be pressed by Karabiner to get action.
 For example, symbol `copy` can be translated to `command+c` if user works on enUs keyboard.
 Also `symbol`s are:

- `cut`

- `undo`

- `redo`

- ...


### Meta layout

You can generate metalayout to use function `combinate`

### Conditions

Conditions will define on the mapping level

## Issues

### Add support for keymap as a function

--

### It can be useful to save keymaps in different files

It may be useful if condition and modifiers will be saved near to keymap. In that case,
 user will create layout directly, without deviding on keymap, modifiers, condition
 
### Invalid model for conditions

Double `condition`

```json
"conditions" : [ {
        "condition" : {
          "type" : "input_source_if",
          "input_sources" : [ {
            "language" : "en",
            "input_source_id" : "com.apple.keylayout.US"
          } ]
        }
      } ]
```

### Support different kind of symbols

Possible to implement FromSymbol, ToSymbol, VariableSymbol and other