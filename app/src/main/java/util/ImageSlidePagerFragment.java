package util;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.docbizz.R;

/**
 * Created by aravind on 14/5/15.
 */
public class ImageSlidePagerFragment extends Fragment {
        public static final String ARG_PAGE = "page";

        /**
         * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
         */
        private int mPageNumber;

        /**
         * Factory method for this fragment class. Constructs a new fragment for the given page number.
         */
        public static ImageSlidePagerFragment create(int pageNumber) {
            ImageSlidePagerFragment fragment = new ImageSlidePagerFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_PAGE, pageNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public ImageSlidePagerFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mPageNumber = getArguments().getInt(ARG_PAGE);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout containing a title and body text.
            ViewGroup rootView = (ViewGroup) inflater
                    .inflate(R.layout.activity_image_slide, container, false);

            // Set the title view to show the page number.
            switch (getPageNumber()) {
                case 0:((ImageView) rootView.findViewById(R.id.sliding_image)).setImageResource(R.drawable.ic_launcher);
                    break;
                case 1:((ImageView) rootView.findViewById(R.id.sliding_image)).setImageResource(R.drawable.ic_launcher);
                    break;
                case 2:((ImageView) rootView.findViewById(R.id.sliding_image)).setImageResource(R.drawable.ic_launcher);
                    break;
                case 3:((ImageView) rootView.findViewById(R.id.sliding_image)).setImageResource(R.drawable.ic_launcher);
                    break;
                case 4:((ImageView) rootView.findViewById(R.id.sliding_image)).setImageResource(R.drawable.ic_launcher);
                    break;
            }

            return rootView;
        }

        /**
         * Returns the page number represented by this fragment object.
         */
        public int getPageNumber() {
            return mPageNumber;
        }

}

