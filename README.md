
# YesRandom2D

#### by me

this is a thing i making

it is annoying 2d programming language with avoidance of stack data structure for no reason in particular

b

| Command written to C register | What it does ( [any] = read variable ) | Registers modified/read |
| ----------------------------- | -------------------------------------- | ----------------------- |
| Add (“@”) | O = A + C | A, C, O |
| Subtract (“;”) | O = [B] - [R] | B, R, O |
| Multiply (“$”) | o = [N] * [Q] | N, Q, o |
| Conditional Copy (“=”) | If [4]==33: [T]=[E] | 4, E, T
| Conditional Copy (“V”) | If [7] > 3 then [F]=[R] | 7, F, R
| Copy (“q”) | [R]=[W] | R, W
| Input/Output (c=10) | If conditionalResult: Q = Input If !conditionalResult: out( [r] ) else: |position = random value | r, Q
| Goto | Set the pointer to the last instance hovered on by the pointer of the character specified in register j | j
| Open File (“^”) | Reads string terminating at \0 starting in register specified in * and opens the file to read/append mode | * |
| Clear (“m”) | Sets file to be cleared | If there is no file, then do nothing
| Write (“!”) | Appends the character specified in N to the currently opened file. | If there is no file, then do nothing | N |
| Read (“M”) | Read a character from the currently opened file. If file has ended return a 0 but if not and the file reads a 0, return a 1 data is stored into * | * |
| Close (“v”) | Close currently opened file. Saves changes. |





## Todo

- ~~FileLine (2D program reader)~~
- ~~Main (Glue! / Util)~~
- ~~Debug (breakpoints?, logger)~~
- FileIO (file io functionality)
- Interpreter (interpreter, finish file io)
