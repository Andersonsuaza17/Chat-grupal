# Chat grupal en Java usando sockets TCP

## Integrantes
- Integrante 1: [Nombre completo]
- Integrante 2: [Nombre completo]
- Integrante 3: [Nombre completo, si aplica]

## Descripción
Aplicación de chat grupal por consola. Varios clientes se conectan a un servidor TCP, envían mensajes y el servidor los distribuye a los demás clientes conectados.

## Arquitectura cliente-servidor
- El servidor utiliza `ServerSocket` para escuchar conexiones en el puerto `5000`.
- Cada vez que llega una conexión, `accept()` devuelve un `Socket` y el servidor crea un hilo (`Thread`) que atiende a ese cliente.
- El cliente utiliza `Socket` para conectarse al host y puerto del servidor.
- Los mensajes viajan mediante flujos de entrada y salida de texto (`BufferedReader` y `PrintWriter`).
- El servidor distribuye cada mensaje a los demás clientes conectados.

## Concurrencia
El servidor crea un hilo por cada cliente para que una conexión no bloquee la atención de las demás. El conjunto de clientes utiliza `ConcurrentHashMap.newKeySet()` para permitir el manejo concurrente de las conexiones. En el cliente, otro hilo recibe mensajes mientras el hilo principal permite escribir.

## Host y puerto
- Host local: `localhost`
- Puerto TCP: `5000`

## Requisitos
- JDK de Java instalado (recomendado Java 8 o superior).
- Terminal o consola de comandos.

## Compilar
Desde la carpeta del proyecto, ejecuta:

```bash
javac -d out src/ServidorChat.java src/ClienteChat.java
```

## Ejecutar
1. Abre una terminal en la carpeta del proyecto y ejecuta el servidor:

```bash
java -cp out ServidorChat
```

2. Abre una segunda terminal y ejecuta el primer cliente:

```bash
java -cp out ClienteChat
```

3. Abre una tercera terminal y ejecuta el segundo cliente:

```bash
java -cp out ClienteChat
```

Puedes abrir más terminales para conectar más clientes. Para salir de un cliente, escribe `/salir`.

## Ejemplo de intercambio
- Cliente 1 ingresa el nombre `Ana`.
- Cliente 2 ingresa el nombre `Luis`.
- Ana escribe: `Hola, ¿cómo están?`
- El servidor muestra `Ana: Hola, ¿cómo están?` y lo envía a los otros clientes conectados.
- Luis recibe `Ana: Hola, ¿cómo están?`.

El servidor muestra los eventos de conexión, los mensajes y las desconexiones.

## Flujo de comunicación
1. El servidor inicia primero y queda escuchando en el puerto 5000.
2. Cada cliente inicia una conexión TCP con el servidor.
3. El servidor solicita un nombre al cliente.
4. Los clientes envían mensajes al servidor.
5. El servidor distribuye cada mensaje a los demás clientes conectados.
6. Cuando un cliente escribe `/salir`, se cierra esa conexión; el servidor continúa activo para los demás.

## Declaración de uso de inteligencia artificial
**Completar esta sección según el uso real del grupo.** Ejemplo, si corresponde:

> Utilizamos ChatGPT como apoyo para proponer una estructura inicial del proyecto, explicar sockets TCP e hilos, y revisar el código. El grupo compiló y probó el programa, verificó la conexión de al menos dos clientes y revisó el funcionamiento del hilo por cliente. Los integrantes estudiaron el código y pueden explicar sus partes.

No afirmen pruebas o verificaciones que todavía no hayan realizado. Completen los nombres de los integrantes antes de publicar el repositorio.
