As JDK 1.4.0 and 1.4.1 package a older version of apache.org packages this causes conflict with newer Jar with is need for Elixir Report.  

current xerces version : 1.4.4
current xalan version : xalan-j_2_4_1
 


see http://xml.apache.org/~edwingo/jaxp-faq.html

Extract from Apache
Q. Why are there Apache classes in the J2SE 1.4 RI? 

The J2SE 1.4 RI is the first version of the JDK that bundles in an implementation of JAXP 1.1. This allows developers to write applications without having to provide a parser and XSLT processor with their application. However, in some cases, it may create additional problems.

The Sun J2SE 1.4 RI uses Apache software for its implemenation of JAXP 1.1 with package names unchanged from Apache software distributions. This can cause problems, for example, if your application wants to use a newer version of Apache software. Under the Java 2 class loader delegation model, the java launcher's ClassLoader will load the bundled version of a class (in rt.jar) before any other version. Thus, if you place a newer version of xalan.jar in the extensions directory or on your CLASSPATH, then that version will be ignored since the runtime will use the older bundled version instead. As a workaround, see the question on overriding the implementation in JDK 1.4.

The future plan is to rename the org.apache.** packages to be something like com.sun.org.apache.** to fix this problem. In addition, other package-dependent parts of the software may also need to be modified. However, this may not be done until after JDK 1.4.1.
