#Script para preprocesamiento de imagen recibida
#Detecta rostros en una foto, cambia a escala de grises 
#y escala a una imagen de 50x50px que contiene la seccion del 
#rostro de la persona.

import cv2
import sys
from sys import argv
import os

#if(len(argv)<4):
#    print ""
#    print "Argumetos:  path nombreRSal clasificadorXML"
#    print ""
#    exit()
directorioRaiz = argv[1]
nombre         = "imgCargada.jpg"
cascPath       =  "haarcascade_frontalface_default.xml"

print "Analizando " , directorioRaiz
try:
    faceCascade = cv2.CascadeClassifier(cascPath)
    image = cv2.imread(directorioRaiz)
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    faces = faceCascade.detectMultiScale(
        gray,
        scaleFactor=1.3,
        minNeighbors=5,
        minSize=(30, 30),
        flags = cv2.cv.CV_HAAR_SCALE_IMAGE
    )

    print "{0} Rostros encontrados".format(len(faces))
    if(len(faces) > 0):
        print "Aislando areas de rostros..."
        lista_carasG = []
        for (x, y, w, h) in faces:
            rgs = gray[y:y+h, x:x+w]
            lista_carasG.append(rgs)
            #Generar imagenes de todas las caras encontradas
        for rostro in lista_carasG:
            resized_image = cv2.resize(rostro, (50, 50)) 
            #cv2.imwrite(nombre + ".jpg", resized_image)
            cv2.imwrite(nombre, resized_image)
            numRostro+=1
            print "OK"
    else:
        print "No se encontraron rostros"
except:
    print 'Error: ' , sys.exc_info()[0]
