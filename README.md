# RDFTables
This application and library converts delimited value/tabular files, e.g. CSV or TSV, into a variety of RDF serialisation.
Configuration is done by modifying the header of the file to provide Class, Property and Datatype information.
It is intended to be as brief to implement and unintrusive as possible.

Features:
* RDF structure is respected and reported.
* A folder can be coverted into a single or multiple files.
* The table does not have to be regularly formed, i.e. sparse or ragged.
* Gaps and repeated columns/rows will not cause an issue.
* Standard sets of prefixes and datatypes can be added to the predefined set for consistent conversion.
* Range of RDF serialisations.

## File Structure

### Header

Header items are delimited by the reserved "|" (pipe) character.
Column 0 header is treated differently, see below.
Each header column can have one to three items in the order:

1. Property URI (predicate) between the target Object (subject) and this column (object). This relationship can be inverted (e.g. for a Foreign Key) by starting the Property URI with "^", but will be rejected if applied to a Literal/Datatype column.
2. Datatype or Class. If not specified then an Object with no class is assumed. This allows Class to be inferred from the schema, asserted in another file or provided elsewhere. Class names are distinguished from Datatypes by ":",
e.g. ":test" uses the base URI to form a Class for an Object, while "test" uses the base URI to form a Datatype.
e.g. ":my:test" uses the prefix "my" to form a Class for an Object, while ":http://example.org/my#test" explictly forms a Class.
3. [OPTIONAL] The target column can be specified as an integer value (default: 0) to allow Properties to be added to Objects within the file.

#### Column 0
The first column contains the base URI and the Class of the column.
The base URI is used as the default prefix for Objects, Properties, Classes and Datatypes that are not URIs and do not have a prefix.

### Data
The first column __MUST__ be an Object.
The remainder of the data can be Objects (forming ObjectProperty relationships) or Literals (forming DatatypeProperty relationships).
Gaps for columns are ignored with no warnings.
Multiple columns with the same or similar items do not cause any issues.

Objects:
*  Explicit URIs are preserved unchanged, e.g. "http://example.org/my#ClassA".
*  Prefixes are expanded using the loaded prefixes, e.g. "my:ClassA" becomes "http://example.org/my#ClassA".
*  All other cases the base URI is applied, e.g. "ClassA" becomes "http://example.org/my#ClassA".

Objects will be created by default with rdfs:label using the local name portion of the URI, but can be switched off globally.
It can also be switched on globablly for all Objects to be created as a members of the class owl:NamedIndividual, but switched off by default.

Literals:
* The Datatype URI specified in the column header is applied with the data in the cell to form a Literal.

## Getting Started
RDFTables can be accessed as a library using Maven etc. from Maven Central or on the command line.

```
<dependency>
    <groupId>io.github.galbiston</groupId>
    <artifactId>rdf-tables</artifactId>
    <version>1.0.2</version>
</dependency>
```

### API
File and Folder conversion methods are contained in the `FileReader` class.
Arguments follow the same conventions as the command line arguments below.

### Command Line Arguments

#### 1) Input File/Folder
```
--input, -i
```

The source for the conversion process.

#### 2) Output File/Folder
```
--output, -o
```
The destination for the conversion process.
Specifying a folder will re-use the file/s name.
Combining an input folder with an output file will consolidate the output into a single file.

#### 3) Delimiter/Separator Value
```
--delim, -l
```
The column delimiter/separator in the input file.
Defaults to comma but any character string can be used except for reserved characters ":", "^" and "|".
Keywords TAB, SPACE and COMMA are also supported.

#### 4) Output Format/Serialisation
```
--format, -f
```
The file serialistion used for the RDF output.

*  JSON-LD		json-ld
*  NTriples		nt
*  NQUADS		nq
*  RDF/JSON		json-rdf
*  RDF/XML		xml
*  RDF/XML PLAIN		xml-plain
*  RDF/THRIFT			thrift
*  TRIX			trix
*  TRIG			trig
*  Turtle		ttl (Default)

#### 5) Prefixes File
```
--prefixes, -p
```
A file of key=value pairs with no header (Java Properties format).
Key is the prefix label and value is the URI for the prefix.
Defaults to searching the input folder and current directory for "prefixes.prop".

Pre-loaded prefixes:
*  olo	http://purl.org/ontology/olo/core#
*  owl	http://www.w3.org/2002/07/owl#
*  rdf	http://www.w3.org/1999/02/22-rdf-syntax-ns#
*  rdfs	http://www.w3.org/2000/01/rdf-schema#
*  time	http://www.w3.org/2006/time#
*  xsd	http://www.w3.org/2001/XMLSchema#

#### 6) Datatypes File
```
--datatypes, -d
```
A file of key=value pairs with no header (Java Properties format).
Key is the datatype label and value is the URI for the datatype.
Defaults to searching the input folder and current directory for "datatypes.prop".

Pre-loaded XSD datatypes:
*  boolean
*  decimal
*  date
*  dateTime
*  double
*  duration
*  int
*  integer
*  nonNegativeInteger
*  nonPositiveInteger
*  positiveInteger
*  string
*  time

#### 7) OWL NamedIndividual
```
--named, -n
```
Boolean value for creating OWL NamedIndividuals in the data. Default: true

#### 8) Excluded Files
```
--exclude, -x
```
Excluded files not to be used as input from a folder.

#### 9) Properties File
Supply the above parameters as a file:
```console
$ java Main @/tmp/parameters
```

### TDB Compilation
Library methods are provided for compiling to TDB graphstore by named graph, see `TDBBuilder.compileFolder()`.

## Future Work
Items that can be developed based on feedback and other suggestions.
* URI checking is minimal when reading the file data but errors will be thrown when adding to the RDF model.
* Multiple files to separate graphs - e.g. NQUADS serialisation. Graph name would be specified as third item in column 0.
* Read configuration from a property file.
* SHACL integration to importa schema and validate the data structure.

![Powered by Apache Jena](https://www.apache.org/logos/comdev-test/poweredby/jena.png "Powered by Apache Jena")
