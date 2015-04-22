
: change to eclipse output
  cd ..\bin
  
: package jar  
  jar cvMf ShooterJOGL_unsigned.jar de/ res/ META-INF/

: change back to dist dir and copy packaged jar
  cd ..\dist
  move ..\bin\ShooterJOGL_unsigned.jar ShooterJOGL_unsigned.jar

: sign
  jarsigner -keystore E:\eclipse_workspace\Shooter\trunk\dist\keystore.jks -storepass chrisy -keypass chrisy -signedjar ShooterJOGL.jar ShooterJOGL_unsigned.jar Shooter

: delete unsigned
  del ShooterJOGL_unsigned.jar
  
: paws
  pause


: create a keystore before signing
: keytool -genkey -alias Shooter -keyalg RSA -keystore E:\eclipse_workspace\Shooter\trunk\dist\keystore.jks -keysize 2048 -validity 100000
