import requests
from sys import argv

if(len(argv)<4):
    print ""
    print "Argumetos:  nombreArchivo prefijoNombres inicioNumeracion"
    print ""
    exit()



archivo = argv[1]
prefijo = argv[2]
reinicio = int(argv[3])
#


with open(archivo) as f:
    for i,linea in enumerate(f):
    	try:
    		print "Fuente: " , linea
    		cadena =  str(prefijo) + str(reinicio) + "." + linea[-5:]
    		cadena.replace("\n","")
    		im = open(cadena,'wb')
    		im.write(requests.get(linea).content)
    		im.close()
    		print "Generado: " , cadena
    		reinicio +=1	
    	except Exception, e:
    		print "ERROR en ejecucion ..."

