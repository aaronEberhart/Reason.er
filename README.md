# Reason.er

This is my ongoing project to develop an ALC tableau reasoner. 

Currently the random syntax generator is functional and accessible through the main test class. To create a random KnowledgeBase you can either make a new KnowledgeBase(int,int) with the sizes you want and it will build one for you, or you can make it from an ABox and TBox. There are also functions to manually input axioms if you want, though this is rather tedious and not the intent of the project. KnowledgeBases are by default not in normal form. You can add an NNF copy to a KnowledgeBase via the normalize() method. 

The default toString() method displays detailed debugging information, but OWL functional syntax and Description Logic style output can be obtained from the toFSString() and toDLString() methods on a generated Knowledge Base. This is useful primarily when written to a text file. A method is included in in the main class that does file writing for you.

The <a href="https://aaroneberhart.github.io/Reason.er/Javadoc/" target="_blank">Javadoc</a> might be helpful if anything is unclear.

Tips:<ol>
<li>universe/individuals should be > 0 or it may behave erratically (why would you want a KB with no classes anyways?)</li>
<li>quantificationDepth/maxSubExpressions will permit a dramatic increase in the size of expressions at higher values</li>
<li>maxSize is still a little buggy but seems to have a more modest efect on the types expressions than the other two</li>
<li>NUMTESTS is just for repeating the whole generator process if you want to</li>
<li>ABOXSIZE/TBOXSIZE determine the number of expressions created in each respectively</li>
</ol>

There is a folder that contains sample files generated by the syntax generator. This output was reorganized for upload to GitHub but the files themselves have not been altered. 

Settings Used to Produce Sample Output:<ul>
<li>NUMTESTS = 1</li>
<li>ABOXSIZE = 30</li>
<li>TBOXSIZE = 30</li>
<li>quantificationDepth = 10</li>
<li>maxSubExpressions = 10</li>
<li>maxSize = 10</li>
<li>universe = 5</li>
<li>individuals = 5</li>
</ul>


