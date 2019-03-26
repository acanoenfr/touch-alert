package fr.acanoen.touchalert;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment implements LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private Location locationUser;
    private Criteria criteria;
    private LocationManager locationManager;
    private String provider;

    private RecyclerView recyclerView;
    private List<Alert> alertList = new ArrayList<>();

    private TextView textView;

    // private RecyclerView mRecyclerView;

    private OnFragmentInteractionListener mListener;

    private RecyclerViewAdapter recyclerViewAdapter;

    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance() {
        NotificationFragment fragment = new NotificationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        // Inflate the layout for this fragment
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, true);
            locationManager.requestLocationUpdates("network", 400, 1, this);
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.alertsList);
        recyclerViewAdapter = new RecyclerViewAdapter(getContext(), alertList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);
        textView = (TextView) view.findViewById(R.id.loader);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.locationUser = location;
        new getAlerts().execute("https://dev.acanoen.fr/touchalert/public/api/alert");
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        //
    }

    @Override
    public void onProviderEnabled(String s) {
        //
    }

    @Override
    public void onProviderDisabled(String s) {
        //
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Implementation of AsyncTask designed to fetch data from the network.
     */
    private class getAlerts extends AsyncTask<String, Integer, NotificationFragment.getAlerts.Result> {

       /* private DownloadCallback<String> mCallback;
        DownloadTask(DownloadCallback<String> callback) {
            setCallback(callback);
        }
        void setCallback(DownloadCallback<String> callback) {
            mCallback = callback;
        }*/

        /**
         * Wrapper class that serves as a union of a result value and an exception. When the download
         * task has completed, either the result value or exception can be a non-null value.
         * This allows you to pass exceptions to the UI thread that were thrown during doInBackground().
         */
        class Result {
            public String mResultValue;
            public Exception mException;
            public Result(String resultValue) {
                mResultValue = resultValue;
            }
            public Result(Exception exception) {
                mException = exception;
            }
        }

        /**
         * Cancel background network operation if we do not have network connectivity.
         */
        @Override
        protected void onPreExecute() {
        }

        /**
         * Defines work to perform on the background thread.
         */
        @Override
        protected NotificationFragment.getAlerts.Result doInBackground(String... urls) {
            NotificationFragment.getAlerts.Result result = null;
            if (!isCancelled() && urls != null && urls.length > 0) {
                String urlString = urls[0];
                try {
                    URL url = new URL(urlString);
                    String resultString = downloadUrl(url);
                    if (resultString != null) {
                        result = new NotificationFragment.getAlerts.Result(resultString);
                    } else {
                        throw new IOException("No response received.");
                    }
                } catch(Exception e) {
                    result = new NotificationFragment.getAlerts.Result(e);
                }
            }
            return result;
        }

        /**
         * Updates the DownloadCallback with the result.
         */
        @Override
        protected void onPostExecute(NotificationFragment.getAlerts.Result result) {

            JSONArray alerts= null;
            try {
                alerts = new JSONArray(result.mResultValue);
                for(int i=0; i<alerts.length();i++) {
                    JSONObject alert = alerts.getJSONObject(i);
                    Double longitude = Double.parseDouble(alert.getString("longitude"));
                    Double latitude = Double.parseDouble(alert.getString("latitude"));

                    Location l = new Location(alert.getString("name"));
                    l.setLatitude(latitude);
                    l.setLongitude(longitude);
                    float distance = locationUser.distanceTo(l);


                    switch (alert.getString("type")) {

                        case "Danger":
                            alertList.add(new Alert(alert.getString("name"), alert.getString("created_at"), R.drawable.ic_danger));
                            break;

                        case "Evénement":
                            alertList.add(new Alert(alert.getString("name"), alert.getString("created_at"), R.drawable.ic_fireworks));
                            break;

                        case "Promotions":
                            alertList.add(new Alert(alert.getString("name"), alert.getString("created_at"), R.drawable.ic_solde));
                            break;

                        case "Santé":
                            alertList.add(new Alert(alert.getString("name"), alert.getString("created_at"), R.drawable.ic_medical));
                            break;

                        case "Catastrophe":
                            alertList.add(new Alert(alert.getString("name"), alert.getString("created_at"), R.drawable.ic_tsunami));
                            break;

                        case "Autre":
                            alertList.add(new Alert(alert.getString("name"), alert.getString("created_at"), R.drawable.ic_more_horiz_black_24dp));
                            break;

                    }


                    recyclerViewAdapter.notifyDataSetChanged();
                    textView.setText("Alerte à proximité");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        /**
         * Override to add special behavior for cancelled AsyncTask.
         */
        @Override
        protected void onCancelled(NotificationFragment.getAlerts.Result result) {
        }
    }

    private String downloadUrl(URL url) throws IOException {
        InputStream stream = null;
        HttpsURLConnection connection = null;
        String result = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
            // Timeout for reading InputStream arbitrarily set to 3000ms.
            connection.setReadTimeout(3000);
            // Timeout for connection.connect() arbitrarily set to 3000ms.
            connection.setConnectTimeout(3000);
            // For this use case, set HTTP method to GET.
            connection.setRequestMethod("GET");
            // Already true by default but setting just in case; needs to be true since this request
            // is carrying an input (response) body.
            connection.setDoInput(true);
            // Open communications link (network traffic occurs here).
            connection.connect();
            //   publishProgress(DownloadCallback.Progress.CONNECT_SUCCESS);
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();
            if (stream != null) {
                result = readStream(stream, 5000);
            }
        } finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }
    /**
     * Converts the contents of an InputStream to a String.
     */
    public String readStream(InputStream stream, int maxReadSize)
            throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] rawBuffer = new char[maxReadSize];
        int readSize;
        StringBuffer buffer = new StringBuffer();
        while (((readSize = reader.read(rawBuffer)) != -1) && maxReadSize > 0) {
            if (readSize > maxReadSize) {
                readSize = maxReadSize;
            }
            buffer.append(rawBuffer, 0, readSize);
            maxReadSize -= readSize;
        }
        return buffer.toString();
    }

}
