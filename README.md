# SystemClassLoaderProperties

When you want light isolation of "System Properties" by ClassLoader in a retrocompatible way... SystemClassLoaderProperties provides Hierarchical Properties associated to a ClassLoader, maintaining the behavior of standard System.getProperty. Open Source Java project under Apache License v2.0

### Current Stable Version is [1.0.0](https://search.maven.org/#search|ga|1|g%3Aorg.javastack%20a%3Asystemclassloaderproperties)

---

## DOC

#### Usage Example in a standalone Java application (programmatically)

```java
public static void main(final String[] args) {
    // Install SystemClassLoaderProperties
    SystemClassLoaderProperties.getInstance().takeover();
    // Normal Usage
    System.setProperty("xxxxx", "value");
    System.out.println(System.getProperty("xxxxx"));
}
```

#### Usage Example in a standalone Java application (using a [JavaAgent](https://docs.oracle.com/javase/7/docs/api/java/lang/instrument/package-summary.html))

```bash
java -javaagent:<directory>/lib/systemclassloaderproperties-x.x.x.jar com.acme.YourClass
```

#### Usage Example in a Tomcat (using a [JavaAgent](https://docs.oracle.com/javase/7/docs/api/java/lang/instrument/package-summary.html))

```bash
# tomcat/bin/setenv.sh
CATALINA_OPTS="$CATALINA_OPTS -javaagent:<directory>/lib/systemclassloaderproperties-x.x.x.jar"
```

##### Proof Of Concept (Servlet Container / Tomcat)

###### Sample ClassLoader Hierarchy (Tomcat 7)

```java
//      Bootstrap
//          |
//       System
//          |
//       Common
//       /     \
//  Webapp1   Webapp2
```

###### Dummy Test JSP (test.jsp) - show last value

```jsp
<%
// One counter by each Webapp
Integer x = Integer.valueOf(System.getProperty("dummy.test", "0"))+1;
out.print(getClass().getClassLoader() + ":" + System.setProperty("dummy.test", String.valueOf(x)));
%>
```

###### Simple Test (bash/curl)

```bash
for j in a b c; do {
  for i in 1 2; do {
    curl http://localhost:8080/Webapp${i}/test.jsp
  } done
} done
```

###### Output will be:

```text
org.apache.jasper.servlet.JasperLoader@7064ce:null
org.apache.jasper.servlet.JasperLoader@7df6d9:null
org.apache.jasper.servlet.JasperLoader@7064ce:1
org.apache.jasper.servlet.JasperLoader@7df6d9:1
org.apache.jasper.servlet.JasperLoader@7064ce:2
org.apache.jasper.servlet.JasperLoader@7df6d9:2
```

###### Note about Security: You may need a SecurityManager if you want strong [security](http://docs.oracle.com/javase/7/docs/api/java/lang/SecurityManager.html#checkPropertiesAccess()) (this "light isolation" can be easily circumvented in a hostile environment).


---

## MAVEN

    <dependency>
        <groupId>org.javastack</groupId>
        <artifactId>systemclassloaderproperties</artifactId>
        <version>1.0.0</version>
    </dependency>

---
Inspired in [ClassLoader](http://docs.oracle.com/javase/7/docs/api/java/lang/ClassLoader.html), this code is Java-minimalistic version.
