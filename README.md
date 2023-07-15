# DUPLICATE FILES FINDER

## Overview
This app helps you find duplicate files based on content, weight, name and metadata.

## Requirements
* Java 17

## 🏭 Releases
Check [here](https://github.com/illoIvan/DuplicateFilesFinder/releases) latest releases to download

## Requirements to modify the project
* Eclipse IDE for Enterprise Java and Web Developers
* Lombok
* SceneBuilder

## Installation 
1. In the eclipse marketplace install the eclipse plugin **e(fx)clipse 3.8.0**.
2. Download and install **Lombok**.
3. Download and install **SceneBuilder**. In Eclipse, go to Window -> Preferences -> JavaFx and add the SceneBuilder installation path.
4. Run MainApplication.java

## Maven Dependencies
* Openjfx 19.0.2 or higher 
* Apache tika 2.4.1 or higher
* Lombok 1.18.24 or higher

## How to use the app

#### Main screen
Based on all the options, a group of files is created that meet the marked options.

**AddMainFolder:** Opens a new screen to select the folder to search for the files
**See checksum files:** Opens a new screen to select the files and shows a new view to display the basic properties without having to search for any option

**Search options:**

* **Ignore Files:** It allows you to ignore some files such as: system files, files of size zero or by a certain size.
* **Match by:**  You can search for files that are equal to their name, size or checksum

* **Checksum Algorithm:** Algorithm that creates a hash based on the content of files to verify that two files contain the same data. **Notes:** Sometimes two files can be the same but not contain the same, use the different options for the search

* **Similarity:** Calculates the similarity between files by reading their content and based on this, assigns the highest percentage of the file with the greatest similarity. The show Similarity checkbox only shows the similarity of the file as mentioned before but is not used for file grouping.

* **Exclude path:** Allows you to exclude folders from the main path for the grouping of files. The add or remove buttons allow you to add or remove the folders to be excluded

* **Metadata:** A specific search is made for the different metadata of the file type

    * **MP3**: It allows you to search specifically for the different metadata of mp3 files such as genre, album, artist, title and duration

* **Search duplicates files:** Main button to perform the search. Once done, the duplicate files screen will be displayed

You can have several screens open with different searches to be able to compare with other searches

#### Duplicates Files screen

Shows the files that have met the previously marked options.
Those files that you want to delete must be selected by marking the checkbox in the delete column

* **Delete selection:** Deletes those files marked in the table

## Other notes
If the files have different checksum values, but are the same file with the same weight, duration, and metadata, it is possible that the difference in checksum values is due to additional information being added to the files, due to minor changes. in the metadata or in the way the data was stored.


When a file's metadata, such as the file name, creation or modification dates, permissions, etc., is modified, the actual content of the file is not affected. However, some file systems may assign a new unique identifier (inode) to the file when the metadata is changed. This can cause the checksum of the file to change as well, even if the content of the file remains the same.

In short, if a file's metadata is changed in a way that affects the file's unique identifier (as in some file systems), the checksum may also change, even though the actual content of the file hasn't changed. This is because the checksum is calculated taking into account all the data in the file, including the metadata, and any change to it can alter the value of the checksum.

You must use the different search methods combining them or using only some.

## License [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

The code in this repository is covered by the included license.

## Contributing
You can contribute to this project and improve with new ideas. If you find any issues, please post them in the issues section of the repository. Thank you.

