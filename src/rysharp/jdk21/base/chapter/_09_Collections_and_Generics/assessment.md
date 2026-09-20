# Collections and Generics


| #  | Question Type/Description | My Answer | Actual Answer | Code Reference | Notes                                                                                                                                                                        |
|----|---------------------------|-----------|---------------|----------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1  | collections               | af        | af            |                |                                                                                                                                                                              |
| 2  | generics                  | cg        | cg            |                | List.of() creates an immutable list, so `removeIf` would throw an `UnsupportedOperationException`. `List<?>` means it is treated as if all the elements are of type `Object` |
| 3  | deque                     | b         | b             |                |                                                                                                                                                                              |
| 4  | generics                  | bf        | bf            |                |                                                                                                                                                                              |
| 5  | generics                  | b         | b             |                |                                                                                                                                                                              |
| 6  | comparator                | bf        | bf            |                |                                                                                                                                                                              |
| 7  | generics                  | bf        | bf            |                |                                                                                                                                                                              |
| 8  | collections               | b         | e             |                |                                                                                                                                                                              |
| 9  | comparator                | b         | a             |                |                                                                                                                                                                              |
| 10 | generics                  | ab        | abd           |                |                                                                                                                                                                              |
| 11 | collections               | abef      | abef          |                |                                                                                                                                                                              |
| 12 | generics                  | bde       | be            |                |                                                                                                                                                                              |
| 13 | comparable                | d         | c             |                |                                                                                                                                                                              |
| 14 | comparator                | a         | a             |                | When using binarySearch(), the List must be sorted in the same order that the Comparator uses.                                                                               |
| 15 | generics                  | ab        | ab            |                |                                                                                                                                                                              |
| 16 | collections               | acd       | ac            |                | LinkedList implements both List and Queue. The List interface has a method to remove by index. Queue has only the remove by object method.                                   |
| 17 | collections               | a         | e             |                | Map does not have a contains() method. It has containsKey() and containsValue()                                                                                              |
| 18 | collections               | d         | ae            |                | entrySet() returns Entry<..,..>. UnsupportedOperationException is thrown if we try modify List.of or List.copyOf lists (e.g. replaceAll)                                     |
| 19 | generics                  | ab        | b             |                | When using generic types in a method, the generic specification goes before the return type                                                                                  |
| 20 | collections               | f         | f             |                |                                                                                                                                                                              |
| 21 | comparator                | bdf       | bdf           |                |                                                                                                                                                                              |
| 22 | collections               | b         | b             |                | A TreeMap sorts its items in the natural order of keys (not the values).                                                                                                     |
| 23 | collections               | g         | h             |                |                                                                                                                                                                              |

Date Completed: 17-Aug
Minimum Score to Pass: 68% (34/50 in real exam)
Correct: 13 out of 23
Percentage: 57%
