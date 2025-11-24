# flow-forge-sdk: SDK for creating executable entities for FlowForge project

## What it is

Currently, this is what is used to create executable blocks, lines, instances of executable procedures: interfaces (contracts), data types, base implementations.  
Ideally, there are no internal, project-specific dependencies.

Using the SDK, you can create a block of your own unique type, build it, and use it to dynamically create an instance of a block type in the FlowForge project.

## What exists and what doesn't

You can already create blocks that accept parameters as text and return work results as text.  
It is assumed that almost anything can be passed in text format - text itself, json, xml, links, and anything else.

## Quick start

```bash
mvn clean install
```

In your application, in pom.xml:

```xml
<properties>
    ...
    <flow-forge-sdk.version>2.0.5</flow-forge-sdk.version>
</properties>

<dependencies>
    <dependency>
        <groupId>ru.spb.tksoft</groupId>
        <artifactId>flow-forge-sdk</artifactId>
        <version>${flow-forge-sdk.version}</version>
    </dependency>
    ...
</dependencies>
```

See example implementation <link to example implementation of your own unique block type>

## Licensing

This module is distributed under the Apache 2.0 license. For details, see the LICENSE file.

## Author

Konstantin Terskikh  
Email: <kostus.online.1974@yandex.ru>, <kostus.online@gmail.com>  
Saint Petersburg, 2025

