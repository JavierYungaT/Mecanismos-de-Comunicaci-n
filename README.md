# Mecanismos de Comunicación

Se puede hablar de sistemas distribuidos, si es posible la comunicación entre procesos corriendo en diferentes espacios de direcciones, estando estos muy probablemente en diferentes máquinas. Es posible construir un sistema básico de comunicación mediante el uso de sockets, los cuales son lo suficientemente flexibles para establecer dicha comunicación. Sin embargo, su uso implica la necesidad de implementar un protocolo que permita el intercambio de información entre los procesos, siendo esto un obstaculo, al ser necesario su diseñ junto con los procesos que van a comunicarse entre si.

## Java™ Remote Method Invocarion, rmi

Java™ RMI es un mecanismo que permite a una aplicación invocar métodos de un objeto que existen en otro espacio de direcciones. Este a su vez puede localizarse a su vez en la misma máquina o en otra remota. Este mecanismo es básicamente, equivalente al anteriormente descrito, rpc, con la salvedad de ser orientado a objetos.

Java™ rmi difiere de corba en un número de aspectos:

- Corba es independiente del lenguaje de programación.

- Incluye otros mecanismos en el estándard, que no se encuentran presentes en rmi.

- No existe un elemento semejante al object request broker en Java™ rmi.

## Procedimiento

El funcionamiento de rmi es como sigue:

El cliente Java™ comienza la invocación remota pidiendo al registro la localización de dicho objeto. Una vez que se tiene esta localización, rmi descarga un stub que puede usar para llamar a los métodos remotos del objeto. Esta característica es la que le diferencia del resto de mecanismos que utilizan esta aproximación y que requieren que el stub resida de forma local previamente a cualquier tipo de invocación remota.


