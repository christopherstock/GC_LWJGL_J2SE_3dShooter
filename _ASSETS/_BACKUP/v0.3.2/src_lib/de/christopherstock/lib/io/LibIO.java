/*  $Id: LibIO.java 182 2010-11-13 13:33:42Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.io;

    import  java.io.*;

    /**************************************************************************************
    *   The GL-View.
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
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
                catch ( Exception u ) {}
                e.printStackTrace();
                return null;
            }
        }
    }
