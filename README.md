# refactoring-examples
Gallery of different refactoring examples for demonstration purpose and display the work done.

## Companyinfo
This is a general project which convert a xml file to a JSON object.
This JSON object should be a specific format as this is accepted by the receiving party (S4H).

This example demonstrates how to old code is refactored to a more readable and less complex solution.
Which can enable new developers to better understand the code and be less intimidated by the code.

### Old statistics
#### JsonUtilsS4H
![JsonUtilsS4H.png](images/companyinfo/JsonUtilsS4H.png)
- 2 main methods with a high complexity score
  - ![s4hMappingComplexity.png](images/companyinfo/getS4HJSONMappingComplexity.png)
  - ![parseJSONForCreate.png](images/companyinfo/parseJSONForCreate.png)

The goal is to reduce the complexity of these methods.
As displayed above, the parameters for the method can also be changed as the whole calling of the different methods are also changed.
Luckily there were some tests with different input validations which helps with the refactoring.

The old situation can be found in the project '**companyinfo**' within the package '**old**'.
