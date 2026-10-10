# Formatting & Localisation

[🔙 Back](README.md)

A quick reference for number formatting, custom decimal patterns and resource bundle lookup.

## Contents

- [NumberFormat](#numberformat)
- [Formatting Numbers](#formatting-numbers)
- [Parsing Numbers](#parsing-numbers)
- [DecimalFormat](#decimalformat)
- [DecimalFormat Pattern Symbols](#decimalformat-pattern-symbols)
- [Resource Bundles](#resource-bundles)
- [Resource Bundle Naming](#resource-bundle-naming)
- [Resource Bundle Lookup](#resource-bundle-lookup)
- [Reading Resource Bundles](#reading-resource-bundles)
- [Quick Reference](#quick-reference)
- [Final Memory Kicks](#final-memory-kicks)

---

## NumberFormat

`NumberFormat` formats and parses numbers according to a locale.

Important factory methods:

```java
NumberFormat.getInstance();
NumberFormat.getNumberInstance();
NumberFormat.getCurrencyInstance();
NumberFormat.getPercentInstance();
NumberFormat.getIntegerInstance();
```

A `Locale` can be supplied:

```java
NumberFormat.getNumberInstance(Locale.US);
NumberFormat.getCurrencyInstance(Locale.UK);
NumberFormat.getPercentInstance(Locale.GERMANY);
```

`getInstance()` and `getNumberInstance()` are effectively the general-purpose
number formatters.

---

## Formatting Numbers

### Number

```java
double value = 1234567.89;

NumberFormat formatter =
        NumberFormat.getNumberInstance(Locale.US);

System.out.println(
        formatter.format(value)
);
```

Produces a locale-appropriate representation such as:

```text
1,234,567.89
```

A different locale can use different grouping and decimal separators.

---

### Currency

```java
double value = 1234.56;

NumberFormat formatter =
        NumberFormat.getCurrencyInstance(Locale.UK);

System.out.println(
        formatter.format(value)
);
```

Produces a UK currency representation such as:

```text
£1,234.56
```

The locale determines formatting conventions and currency information.

---

### Percentage

```java
double value = 0.25;

NumberFormat formatter =
        NumberFormat.getPercentInstance(Locale.US);

System.out.println(
        formatter.format(value)
);
```

Produces:

```text
25%
```

Important:

```text
0.25
→ 25%
```

The input represents the proportion, not the already-multiplied percentage.

---

### Factory Method Memory

```text
getNumberInstance()
→ ordinary number

getCurrencyInstance()
→ currency

getPercentInstance()
→ percentage

getIntegerInstance()
→ integer formatting
```

---

## Parsing Numbers

`NumberFormat` works in both directions:

```text
NUMBER
   ↓ format()
STRING
```

and:

```text
STRING
   ↓ parse()
NUMBER
```

For example:

```java
NumberFormat formatter =
        NumberFormat.getNumberInstance(Locale.US);

Number number =
        formatter.parse("1,234.56");

System.out.println(number);
// 1234.56
```

Notice the return type:

```text
Number
```

not necessarily:

```text
Double
Integer
Long
```

Therefore this is valid:

```java
Number value =
        formatter.parse("1,234.56");
```

`parse()` can throw:

```text
ParseException
```

so it must be handled or declared.

---

## DecimalFormat

`DecimalFormat` allows a custom numeric pattern to be specified.

```java
DecimalFormat formatter =
        new DecimalFormat("$000.00");

System.out.println(
        formatter.format(2.2)
);
```

Output:

```text
$002.20
```

The two most important pattern symbols are:

```text
#
0
```

---

## DecimalFormat Pattern Symbols

### `#` — Optional Digit

`#` displays a digit if one exists but does not add a zero merely to fill the
position.

```java
DecimalFormat formatter =
        new DecimalFormat("$###.##");

System.out.println(
        formatter.format(2.2)
);
```

Output:

```text
$2.2
```

Think:

```text
#
→ optional position
→ don't pad with zero
```

---

### `0` — Required Digit

`0` displays a digit if one exists and inserts `0` when necessary to fill the
position.

```java
DecimalFormat formatter =
        new DecimalFormat("$000.00");

System.out.println(
        formatter.format(2.2)
);
```

Output:

```text
$002.20
```

Think:

```text
0
→ required position
→ pad with zero
```

---

### Compare Them

For:

```text
value = 2.2
```

| Pattern | Result |
|---|---|
| `$###.##` | `$2.2` |
| `$000.00` | `$002.20` |

Memory:

```text
#
→ digit IF NEEDED

0
→ digit REQUIRED
→ zero if missing
```

---

### Decimal Point

`.` separates the integer and fractional portions of the pattern.

```text
###.##
```

Conceptually:

```text
integer
 ↓
###
   .
   ↑
decimal separator
    ↓
    ##
fraction
```

---

### Grouping Separator

`,` specifies grouping.

For example:

```java
DecimalFormat formatter =
        new DecimalFormat("#,###.00");

System.out.println(
        formatter.format(12345.6)
);
```

Produces:

```text
12,345.60
```

Pattern:

```text
#,###.00
```

means:

```text
,
→ grouping

.
→ decimal separator

#
→ optional digit

0
→ required digit
```

---

## Resource Bundles

Resource bundles allow an application to load different values depending on
the requested locale.

A common use is translated text.

For example:

```text
Welcome.properties
Welcome_en.properties
Welcome_en_GB.properties
Welcome_fr.properties
```

Each bundle can contain entries such as:

```properties
greeting=Hello
```

or:

```properties
greeting=Bonjour
```

Java chooses the most appropriate resource bundle for the requested locale.

---

## Resource Bundle Naming

The general naming pattern is:

```text
BaseName.properties

BaseName_language.properties

BaseName_language_COUNTRY.properties
```

For example:

```text
Zoo.properties
Zoo_en.properties
Zoo_en_GB.properties
Zoo_en_US.properties
Zoo_fr.properties
Zoo_fr_FR.properties
```

Locale components use:

```text
language
→ lowercase

COUNTRY
→ uppercase
```

Examples:

```text
en
fr
de

GB
US
FR
```

Therefore:

```text
en_GB
en_US
fr_FR
```

---

## Resource Bundle Lookup

Suppose the requested locale is:

```java
Locale locale =
        new Locale("en", "GB");

ResourceBundle bundle =
        ResourceBundle.getBundle(
                "Zoo",
                locale
        );
```

The requested locale is:

```text
en_GB
```

Java looks for increasingly general candidates.

Conceptually:

```text
Zoo_en_GB
    ↓
Zoo_en
    ↓
Zoo
```

The most specific available match is preferred.

---

### Example

Suppose these files exist:

```text
Zoo.properties
Zoo_en.properties
Zoo_en_GB.properties
```

Request:

```text
en_GB
```

Most specific match:

```text
Zoo_en_GB.properties
```

---

### Missing Country-Specific Bundle

Suppose:

```text
Zoo.properties
Zoo_en.properties
```

exist, but:

```text
Zoo_en_GB.properties
```

does not.

Request:

```text
en_GB
```

Java can fall back:

```text
Zoo_en_GB   ❌
     ↓
Zoo_en      ✅
```

So:

```text
Zoo_en.properties
```

is used.

---

### Missing Language Bundle

Suppose only:

```text
Zoo.properties
```

exists.

Request:

```text
en_GB
```

The hierarchy reaches:

```text
Zoo_en_GB   ❌
     ↓
Zoo_en      ❌
     ↓
Zoo         ✅
```

The base bundle is used.

---

### Requested Locale vs Default Locale

There is an extra part of resource-bundle lookup worth remembering.

If Java cannot find an appropriate bundle for the **requested locale**, it may
try candidates based on the JVM's **default locale** before finally using the
base bundle.

Conceptually:

```text
REQUESTED LOCALE CANDIDATES
        ↓
DEFAULT LOCALE CANDIDATES
        ↓
BASE BUNDLE
```

For example, imagine:

```text
requested locale = fr_FR
default locale   = en_GB
```

and available bundles are:

```text
Zoo.properties
Zoo_en.properties
Zoo_en_GB.properties
```

There is no French bundle.

Java can therefore fall back through the default locale candidates:

```text
requested:

Zoo_fr_FR ❌
Zoo_fr    ❌

        ↓

default:

Zoo_en_GB ✅
```

So the selected bundle can be:

```text
Zoo_en_GB.properties
```

rather than immediately jumping to:

```text
Zoo.properties
```

Memory:

```text
REQUESTED
    ↓
DEFAULT
    ↓
BASE
```

---

### Parent Bundles

Selecting a specific bundle does not mean values can only come from that
single file.

Suppose:

```properties
# Zoo.properties
open=Open
close=Close
```

```properties
# Zoo_en.properties
open=Open now
```

```properties
# Zoo_en_GB.properties
open=We're open
```

For locale:

```text
en_GB
```

the hierarchy is:

```text
Zoo_en_GB
    ↓ parent
Zoo_en
    ↓ parent
Zoo
```

Looking up:

```java
bundle.getString("open");
```

finds it immediately in:

```text
Zoo_en_GB
```

But:

```java
bundle.getString("close");
```

can inherit the value from:

```text
Zoo.properties
```

Think:

```text
BUNDLE SELECTION
→ choose most appropriate bundle

KEY LOOKUP
→ selected bundle
→ then its parents
```

This distinction is important.

---

## Reading Resource Bundles

Load a bundle with:

```java
ResourceBundle bundle =
        ResourceBundle.getBundle(
                "Zoo",
                Locale.UK
        );
```

Read a value with:

```java
String value =
        bundle.getString("greeting");
```

For example:

```java
System.out.println(
        bundle.getString("greeting")
);
```

---

### Missing Bundle

If Java cannot find an appropriate resource bundle:

```text
MissingResourceException
```

is thrown.

---

### Missing Key

If the bundle hierarchy exists but the requested key cannot be found:

```java
bundle.getString("missingKey");
```

also results in:

```text
MissingResourceException
```

Memory:

```text
bundle missing
→ MissingResourceException

key missing
→ MissingResourceException
```

---

## Quick Reference

### NumberFormat

```text
getNumberInstance()
→ number

getCurrencyInstance()
→ currency

getPercentInstance()
→ percentage

getIntegerInstance()
→ integer
```

```text
format(number)
→ String

parse(String)
→ Number
→ may throw ParseException
```

---

### Percent

```text
0.25
→ 25%
```

not:

```text
25
→ 25%
```

---

### DecimalFormat

```text
#
→ optional digit
→ no zero padding

0
→ required digit
→ zero padding
```

```text
2.2

$###.##
→ $2.2

$000.00
→ $002.20
```

```text
,
→ grouping

.
→ decimal position
```

---

### Resource Bundle Names

```text
Zoo.properties
Zoo_en.properties
Zoo_en_GB.properties
```

Think:

```text
BASE
↓
LANGUAGE
↓
LANGUAGE + COUNTRY
```

Specificity increases downward.

---

### Resource Bundle Search

For requested:

```text
en_GB
```

first consider:

```text
Zoo_en_GB
↓
Zoo_en
```

If the requested locale cannot provide an appropriate bundle, Java can try
the default locale candidates.

Ultimately:

```text
Zoo
```

is the base bundle.

High-level memory:

```text
REQUESTED LOCALE
      ↓
DEFAULT LOCALE
      ↓
BASE
```

---

### Bundle vs Key Lookup

```text
FIRST:
find the best BUNDLE

THEN:
find the KEY through its parent hierarchy
```

For:

```text
Zoo_en_GB
```

key lookup can move through:

```text
Zoo_en_GB
↓
Zoo_en
↓
Zoo
```

---

### Exceptions

```text
no suitable bundle
→ MissingResourceException

key absent from hierarchy
→ MissingResourceException

NumberFormat.parse()
→ ParseException
```

---

## Final Memory Kicks

```text
NUMBER FORMAT:

number
→ getNumberInstance

money
→ getCurrencyInstance

percentage
→ getPercentInstance
```

```text
FORMAT:
Number → String

PARSE:
String → Number
```

```text
DECIMAL FORMAT:

#
→ OPTIONAL

0
→ REQUIRED


2.2

###.##
→ 2.2

000.00
→ 002.20
```

```text
PERCENT:

0.25
→ 25%
```

```text
RESOURCE BUNDLE NAME:

Zoo
Zoo_en
Zoo_en_GB

GENERAL
→ SPECIFIC
```

```text
REQUESTED en_GB:

Zoo_en_GB
↓
Zoo_en
↓
fallback processing
```

```text
BIG FALLBACK MEMORY:

REQUESTED
↓
DEFAULT
↓
BASE
```

```text
AFTER A BUNDLE IS SELECTED:

specific bundle
↓
parent
↓
parent

until key found
```

```text
MISSING:

bundle
→ MissingResourceException

key
→ MissingResourceException
```