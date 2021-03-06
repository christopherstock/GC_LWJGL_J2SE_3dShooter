/*  $Id: ShooterSound.java 1275 2014-07-02 06:16:12Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.io.datasource;

    import  javax.media.protocol.ContentDescriptor;
    import  javax.media.protocol.PullDataSource;
    import  java.nio.ByteBuffer;
    import  java.io.IOException;
    import  javax.media.Duration;

    /**************************************************************************************
    *   A DataSource for the JFM.
    *
    *   @version    0.3.11
    *   @author     Christopher Stock
    **************************************************************************************/
    public class LibIODataSource extends PullDataSource
    {
        protected   ContentDescriptor           contentType;

        protected   LibIOSeekableStream[]       sources;

        protected   ByteBuffer                  anInput;

        protected   LibIODataSource()
        {
        }

        /**************************************************************************************
        *   Construct a <CODE>ByteBufferDataSource</CODE> from a <CODE>ByteBuffer</CODE>.
        *
        *   @param source The <CODE>ByteBuffer</CODE> that is used to create the the <CODE>DataSource</CODE>.
        **************************************************************************************/
        public LibIODataSource(ByteBuffer input, String contentType)
        {
            anInput = input;
            this.contentType = new ContentDescriptor( contentType );

            sources = new LibIOSeekableStream [ 1 ];
            sources[ 0 ] = new LibIOSeekableStream( anInput );
        }

        /**************************************************************************************
        *   Open a connection to the source described by the <CODE>ByteBuffer/CODE>.
        *   The <CODE>connect</CODE> method initiates communication with the source.
        *
        *   @exception IOException Thrown if there are IO problems when <CODE>connect</CODE> is called.
        **************************************************************************************/
        @Override
        public void connect() throws java.io.IOException
        {
        }

        /**************************************************************************************
        *   Close the connection to the source described by the locator.
        *   <p>
        *   The <CODE>disconnect</CODE> method frees resources used to maintain a
        *   connection to the source.
        *   If no resources are in use, <CODE>disconnect</CODE> is ignored.
        *   If <CODE>stop</CODE> hasn't already been called,
        *   calling <CODE>disconnect</CODE> implies a stop.
        **************************************************************************************/
        @Override
        public void disconnect()
        {
            sources[ 0 ].close();
        }

        /**************************************************************************************
        *   Get a string that describes the content-type of the media
        *   that the source is providing.
        *   <p>
        *   It is an error to call <CODE>getContentType</CODE> if the source is
        *   not connected.
        *
        *   @return The name that describes the media content.
        **************************************************************************************/
        @Override
        public String getContentType()
        {
            return contentType.getContentType();
        }

        @Override
        public Object getControl( String str )
        {
            return null;
        }

        @Override
        public Object[] getControls()
        {
            return new Object[ 0 ];
        }

        @Override
        public javax.media.Time getDuration()
        {
            return Duration.DURATION_UNKNOWN;
        }

        /**************************************************************************************
        *   Get the collection of streams that this source
        *   manages. The collection of streams is entirely
        *   content dependent. The  MIME type of this
        *   <CODE>DataSource</CODE> provides the only indication of
        *   what streams can be available on this connection.
        *
        *   @return The collection of streams for this source.
        **************************************************************************************/
        @Override
        public javax.media.protocol.PullSourceStream[] getStreams()
        {
            return sources;
        }

        /**************************************************************************************
        *   Initiate data-transfer. The <CODE>start</CODE> method must be
        *   called before data is available.
        *   (You must call <CODE>connect</CODE> before calling <CODE>start</CODE>.)
        *
        *   @exception IOException Thrown if there are IO problems with the source when <CODE>start</CODE> is called.
        **************************************************************************************/
        @Override
        public void start() throws IOException
        {
        }

        /**************************************************************************************
        *   Stop the data-transfer. If the source has not been connected and started,
        *   <CODE>stop</CODE> does nothing.
        **************************************************************************************/
        @Override
        public void stop() throws IOException
        {
        }
    }
