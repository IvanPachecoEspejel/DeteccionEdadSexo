import cv2
import sys
from sys import argv
import os

if(len(argv)):
    print ""
    print "Argumetos:  dirDeBusqueda prefijoNombres inicioNumeracion clasificadorXML"
    print ""
    exit()

directorioRaiz = argv[1]
prefijo = argv[2]
numRostro = int(argv[3])

imagenes = []

#def analizadorDirectorio():
#    global imagenes
#    global directorioRaiz
i = 0
for file in os.listdir(directorioRaiz):  #Imagenes
    print "Analizando archivo.." , file
    #if file.endswith(".jpg"):
    #if file.startswith("WMN"):
    if file.startswith("HM"):
        print 'img',i,':',file
        imagenes.append(directorioRaiz + "/" + file)
        i +=1

#def analizarImagen():
#global numRostro
#global imagenes
#global prefijo
## Get user supplied values
##imagePath = sys.argv[1]
##cascPath = sys.argv[2]
cascPath = sys.argv[4]
for imagen in imagenes:
    try:
        print "-------------------------------------"
        # Create the haar cascade
        faceCascade = cv2.CascadeClassifier(cascPath)
        # Read the image
        image = cv2.imread(imagen)
        gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
        # Detect faces in the image
        faces = faceCascade.detectMultiScale(
            gray,
            scaleFactor=1.2,
            minNeighbors=5,
            minSize=(30, 30),
            flags = cv2.cv.CV_HAAR_SCALE_IMAGE
        )
        print "Found {0} faces!".format(len(faces))
        if(len(faces) > 0):
            print "Aislando areas de rostro..."
            lista_rois = []
            # Draw and isolate a rectangle correspondig to the faces
            for (x, y, w, h) in faces:
                cv2.rectangle(image, (x, y), (x+w, y+h), (0, 255, 0), 2)
                roi = gray[y:y+h, x:x+w]
                lista_rois.append(roi)
            #cv2.imshow("Faces found", image)
            print "Creando nuevas imagenes de rostros..."
            for rostro in lista_rois:
                #cv2.imshow("Ventana" + str(numRostro), rostro)
                print "NO dibujado" , ".jpg", numRostro
                str(numRostro) + ".jpg"
                cv2.imwrite(prefijo + str(numRostro) + ".jpg", rostro)
                print "Dibujado"
                numRostro+=1
            #cv2.waitKey(0)
            print imagen, '...OK'     
    except:
        print 'Error al procesar imagen' , sys.exc_info()[0]


#analizadorDirectorio()
#analizarImagen()

#Referencia:
#https://realpython.com/blog/python/face-recognition-with-python/

