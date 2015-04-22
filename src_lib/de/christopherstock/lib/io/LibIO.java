/*  $Id: LibIO.java 1304 2015-02-12 19:03:53Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.io;

    import  java.io.*;
    import  java.nio.ByteBuffer;
    import  java.util.*;
    import  de.christopherstock.lib.*;

    /**************************************************************************************
    *   The GL-View.
    *
    *   @author     Christopher Stock
    *   @version    0.3.11
    **************************************************************************************/
    public abstract class LibIO
    {
        /*****************************************************************************************//**
        *   Reads the given {@link InputStream} buffered and returns all read bytes as a byte-array.
        *
        *   @param  is      The InputStream to read buffered.
        *   @return         All bytes of the given InputStream as a byte-array.
        *********************************************************************************************/
        public static byte[] readStreamBuffered( InputStream is )
        {
            ByteArrayOutputStream   baos        = new ByteArrayOutputStream();
            int                     byteread    = 0;

            try
            {
                //read one byte after another until the EOF-flag is returned
                while ( ( byteread = is.read() ) != -1 )
                {
                    //write this byte, if it could be read, into the output-stream
                    baos.write( byteread );
                }

                is.close();
                baos.close();

                //return the output-stream as a byte-array
                return baos.toByteArray();
            }
            catch ( Exception e )
            {
                try
                {
                    is.close();
                    baos.close();
                }
                catch ( Exception u )
                {
                    //ignore exceptions
                }
                e.printStackTrace();
                return null;
            }
        }

        public static final void saveObjects( String filename, Vector<Object> objectsToSave ) throws Throwable
        {
            FileOutputStream   fos = new FileOutputStream(   filename   );
            ObjectOutputStream oos = new ObjectOutputStream( fos        );

            for ( Object o : objectsToSave )
            {
                oos.writeObject( o );
            }

            oos.close();
        }

        public static final Object[] loadObjects( String filename ) throws Throwable
        {
            Vector<Object>     ret = new Vector<Object>();

            FileInputStream    fis = new FileInputStream(    filename   );
            ObjectInputStream  ois = new ObjectInputStream(  fis        );

            try
            {
                //isn't there an other way?
                while ( true )
                {
                    Object o = ois.readObject();
                    ret.add( o );
                }
            }
            catch ( EOFException eof )
            {
            }
            finally
            {
                ois.close();
            }

            return ret.toArray( new Object[] {} );
        }

        public static final ByteArrayInputStream preStreamJarResource( String url ) throws IOException
        {
            InputStream             in      = Thread.currentThread().getClass().getResourceAsStream( url );
            ByteArrayOutputStream   byteOut = new ByteArrayOutputStream();
            byte[]                  buffer  = new byte[ Lib.JAR_BUFFER_SIZE ];
            for ( int len; ( len = in.read( buffer ) ) != -1; ) byteOut.write( buffer, 0, len );
            in.close();
            ByteArrayInputStream    byteIn = new ByteArrayInputStream( byteOut.toByteArray() );
            return byteIn;
        }

        public static final ByteBuffer createByteBufferFromByteArray( byte[] bytes )
        {
            // Wrap a byte array into a buffer
            return ByteBuffer.wrap(bytes);
        }
    }
