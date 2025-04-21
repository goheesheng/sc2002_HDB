Prerequisites Installation:
- Install Java 17 (JDK 17) since the project uses Java 17 features
- Install Maven (latest version) for dependency management and building
- Install an IDE (like IntelliJ IDEA, Eclipse, or VS Code with Java extensions)

The project uses several key dependencies managed by Maven:
- Apache POI for Excel file handling
- Apache PDFBox for PDF generation
- SLF4J and Log4j for logging
- All dependencies are defined in pom.xml, so Maven will handle downloading them

  
Building the Project:
- mvn clean install
- java -jar target/hdb-system-1.0-SNAPSHOT.jar