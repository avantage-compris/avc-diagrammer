# avc-diagrammer

Ad hoc tools in Java that generate SVG diagrams.

## Hello World!

The following program:

    public class MyFirstDiagram extends AvcDiagrammer {

        public static void main(final String... args) {
        
            new MyFirstDiagram().run(160, 80);
        }
        
        @Override
        protected void body() {
        
            box(10, 10, 100, "#69f", "Hello", "*World!*");
        }
    }
    
will output to stdout the following image (in SVG):

[MyFirstDiagram.svg: "Hello World!" in a box (SVG)](src/site/resources/images/MyFirstDiagram.svg)

![MyFirstDiagram.png: "Hello World!" in a box](src/site/resources/rasterized/MyFirstDiagram.png)

Java code: [MyFirstDiagram.java](src/test/java/net/avcompris/tools/diagrammer/sample/MyFirstDiagram.java)

## Samples

  * [AppCenters.svg: App Centers (SVG)](src/site/resources/images/AppCenters.svg)
  
    <!-- ![AppCenters.svg: App Centers](src/site/resources/rasterized/AppCenters.png =100x) -->

    <img alt="AppCenters.svg: App Centers" src="src/site/resources/rasterized/AppCenters.png" width="100px">
  
    Java code: [AppCenters.java](src/test/java/net/avcompris/tools/diagrammer/sample/AppCenters.java)

  * [DefaultArchitecture.svg: Default Architecture (SVG)](src/site/resources/images/DefaultArchitecture.svg)
    
    <!-- ![DefaultArchitecture.svg: App Centers](src/site/resources/rasterized/DefaultArchitecture.png =100x) -->

    <img alt="DefaultArchitecture: Default Architecture" src="src/site/resources/rasterized/DefaultArchitecture.png" width="100px">
  
    Java code: [DefaultArchitecture.java](src/test/java/net/avcompris/tools/diagrammer/sample/DefaultArchitecture.java)

  * [MigrationScenario1_1.svg: Migration Scenario #1.1 (SVG)](src/site/resources/images/MigrationScenario1_1.svg)

    <img alt="Migration Scenario #1.1" src="src/site/resources/rasterized/MigrationScenario1_1.png" width="100px">
  
    Java code: [MigrationScenario1_1.java](src/test/java/net/avcompris/tools/diagrammer/sample/MigrationScenario1_1.java)
    
  * [MigrationScenario1_2.svg: Migration Scenario #1.2 (SVG)](src/site/resources/images/MigrationScenario1_2.svg)

    <img alt="Migration Scenario #1.2" src="src/site/resources/rasterized/MigrationScenario1_2.png" width="100px">
  
    Java code: [MigrationScenario1_2.java](src/test/java/net/avcompris/tools/diagrammer/sample/MigrationScenario1_2.java)

  * [MigrationScenario1_3.svg: Migration Scenario #1.3 (SVG)](src/site/resources/images/MigrationScenario1_3.svg)

    <img alt="Migration Scenario #1.3" src="src/site/resources/rasterized/MigrationScenario1_3.png" width="100px">
  
    Java code: [MigrationScenario1_3.java](src/test/java/net/avcompris/tools/diagrammer/sample/MigrationScenario1_3.java)

  * [MigrationScenario1_4.svg: Migration Scenario #1.4 (SVG)](src/site/resources/images/MigrationScenario1_4.svg)

    <img alt="Migration Scenario #1.4" src="src/site/resources/rasterized/MigrationScenario1_4.png" width="100px">
  
    Java code: [MigrationScenario1_4.java](src/test/java/net/avcompris/tools/diagrammer/sample/MigrationScenario1_4.java)

  * [MigrationScenario2_1.svg: Migration Scenario #1.4 (SVG)](src/site/resources/images/MigrationScenario2_1.svg)

    <img alt="Migration Scenario #2.1" src="src/site/resources/rasterized/MigrationScenario2_1.png" width="100px">
  
    Java code: [MigrationScenario2_1.java](src/test/java/net/avcompris/tools/diagrammer/sample/MigrationScenario2_1.java)

  * [MigrationScenario2_2.svg: Migration Scenario #1.4 (SVG)](src/site/resources/images/MigrationScenario2_2.svg)

    <img alt="Migration Scenario #2.2" src="src/site/resources/rasterized/MigrationScenario2_2.png" width="100px">
  
    Java code: [MigrationScenario2_2.java](src/test/java/net/avcompris/tools/diagrammer/sample/MigrationScenario2_2.java)

  * [MigrationScenario2_3.svg: Migration Scenario #1.4 (SVG)](src/site/resources/images/MigrationScenario2_3.svg)

    <img alt="Migration Scenario #2.3" src="src/site/resources/rasterized/MigrationScenario2_3.png" width="100px">
  
    Java code: [MigrationScenario2_3.java](src/test/java/net/avcompris/tools/diagrammer/sample/MigrationScenario2_3.java)

  * [MigrationScenario2_4.svg: Migration Scenario #1.4 (SVG)](src/site/resources/images/MigrationScenario2_4.svg)

    <img alt="Migration Scenario #2.4" src="src/site/resources/rasterized/MigrationScenario2_4.png" width="100px">
  
    Java code: [MigrationScenario2_4.java](src/test/java/net/avcompris/tools/diagrammer/sample/MigrationScenario2_4.java)
                                                                                                