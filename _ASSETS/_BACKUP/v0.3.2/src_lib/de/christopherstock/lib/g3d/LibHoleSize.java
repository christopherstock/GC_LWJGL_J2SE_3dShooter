
    package de.christopherstock.lib.g3d;

    public enum LibHoleSize
    {
        /** for wearpons that do not produce bullet holes. */
        ENone(  0.0f    ),
        ETiny(  0.015f  ),
        ESmall( 0.020f  ),
        E9mm(   0.025f  ),
        E51mm(  0.030f  ),
        EHuge(  0.035f  ),
        ;

        public  float   size    = 0.0f;

        private LibHoleSize( float aSize )
        {
            size = aSize;
        }
    }
