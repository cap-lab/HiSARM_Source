# HiSARM_Source
This is the source repository of HiSARM (https://github.com/cap-lab/HiSARM.git)

## Prerequisite
- Java SDK (version 8 or higher) 
- maven (Tested in version 3.6.3)

## How to Compile and Update Your HiSARM Binaries and Template Files
```bash
git clone https://github.com/cap-lab/HiSARM_Source.git
cd HiSARM_Source 
mvn clean install
cp -rfp CodeGenerator/generator/dist/* {Your HiSARM Directory Path}/.
```

## Explanation of Subdirectories
- CodeGenerator: Generates or copies task code.
- DBManager: Access the database, assuming CouchDB in the current version.
- MetadataGenerator: Generates metadata files including algorithm.xml, architecture.xml, mapping.xml, and configuration.xml.
- ScriptParser: Parses mission scripts.
- Strategy: Maps abstract robots in mission scripts to actual robots.
- XMLParser: Provides a schema to parse task.xml, the task graph format used in HOPES (https://github.com/cap-lab/HOPES.git).
