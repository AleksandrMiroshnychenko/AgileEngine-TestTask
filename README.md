The program takes html element with given or default id from the origin file, then finds and prints the xml path to the most similar element in the target file.
It compares elements by attributes and content.

The most similar element will be the one with the greatest number of the same attributes and content as origin element.

If there are not similar elements, program will print "No similar elements found.".

To run the program give it two or three program arguments:
1) path to origin file;
2) path to target file;
3) id of element to take from origin file (it will be "make-everything-ok-button" by default).

Program will print xml path to the most similar element in the target file.