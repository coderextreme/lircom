There are a variety of things to try from the command-line:
```bash
make impactVL
make division
make 4x4

ant
ant impact
ant Cell
ant Cell4x4

java -cp impact.jar net.coderextreme.impact.Impact
java -cp impact.jar net.coderextreme.impact.Cell
java -cp impact.jar net.coderextreme.impact.Cell Cell4x4

java -jar impact.jar

mvn clean install exec:java
```
```
I am proposing a new mocap format that doesn’t need a hierarchy, and can do streaming, graph or grid data.
S:structure of data, G for grid, H for tree, D for D for DAG, C for cycles
V: name or id of structure 
F: from joint name and/or id
T: to joint name and/or id

O: out name
B: out data (beginning)
I: in name
E: in data  (ending)...etc. 

The animation is done like:
J:Joint id or name
C:Joint class or type (may change)
A:Joint alias or DEF (optional)
X:X location, unit (unit may be defaulted, no unit for scaling)
Y:Y location, unit (ditto)
Z:Z location, unit (ditto)
T:frame or time, unit (ditto)...etc.

To remove joint, bones or routes:
-V: remove structure  
-J: remove joint 
-F: from joint name and/or id
-T: to joint name and/or id

Later, I will provide 4 data points per joint over time, for impact simulator I/O grid
J: Joint, as above
U: up data
D: down data
R: right data
L: left data
```


Plus ways to remove data by adding a - in front or behind the label and a leading or a trailing + to add data (to a tuple)
This needs to be generalized. Think how to do hypergraphs. More verbose would be an option,
IDK, i don’t want to reinvent a document format.

I am proposing a new mocap format that doesn’t need a hierarchy, and can do streaming, graph or grid data.

Inspired by Katy Schildmeyer's Joint Location format.
