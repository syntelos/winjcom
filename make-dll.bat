REM You have to set your java home correctly !!!

C:\home\my-java\mingw\MinGWruntime\bin\gcc -Wall -D_JNI_IMPLEMENTATION_ -Wl,--kill-at -IC:\Programmi\Java\jdk1.6.0_05\include  -IC:\Programmi\Java\jdk1.6.0_05\include\win32 -shared winjcom.c -o winjcom.dll
copy winjcom.dll C:\WINDOWS
