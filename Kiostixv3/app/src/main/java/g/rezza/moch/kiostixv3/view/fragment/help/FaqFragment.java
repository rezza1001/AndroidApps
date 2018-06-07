package g.rezza.moch.kiostixv3.view.fragment.help;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.activity.EventDetailActivity;
import g.rezza.moch.kiostixv3.adapter.FaqAdapter;
import g.rezza.moch.kiostixv3.adapter.SeeAllAdapter;
import g.rezza.moch.kiostixv3.database.BookingDB;
import g.rezza.moch.kiostixv3.database.EventsDB;
import g.rezza.moch.kiostixv3.datastatic.App;
import g.rezza.moch.kiostixv3.holder.EventsList;
import g.rezza.moch.kiostixv3.holder.FaqHolder;

/**
 * Created by rezza on 09/02/18.
 */

public class FaqFragment extends Fragment {
    private static final String TAG = "FaqFragment";

    private ListView lsvw_faq_00;
    private FaqAdapter adapter;
    private ArrayList<FaqHolder> list = new ArrayList<>();

    public static Fragment newInstance() {
        Fragment frag   = new FaqFragment();
        Bundle args     = new Bundle();
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.view_help_faq, container, false);
        lsvw_faq_00     = view.findViewById(R.id.lsvw_faq_00);
        adapter         = new FaqAdapter(getActivity(), list);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lsvw_faq_00.setAdapter(adapter);
        requestData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }



    private void requestData(){
        list.clear();
        {
            FaqHolder faq = new FaqHolder();
            faq.title = getActivity().getResources().getString(R.string.my_account);
            faq.description = "<p><b>Bagaimana cara untuk memiliki Akun Kiostix?</b><br><em>Apabila belum memiliki akun Kiostix, silahkan melakukan registrasi disini.</em><br><br><b>Bagaimana cara masuk ke Akun Kiostix Saya?</b>" +
                    "<br><em>Apabila sudah memiliki akun Kiostix, silahkan masuk dengan meng-klik button \"Masuk\" di bagian atas website Kiostix.</em><br>" +
                    "<br><b>Bagaimana bila saya lupa kata sandi akun Kiostix Saya?</b><br><em>Pada menu Log-In / Masuk, silahkan pilih \"Lupa Kata Sandi\"</em><br>" +
                    "<br><b>Bagaimana cara untuk melihat Daftar Transaksi Saya?</b><br><em>Pada halaman akun, arahkan kursor anda dan pilih menu Transaksi atau Daftar Transaksi</em><br>" +
                    "<br><b>Bagaimana cara saya mengganti kata kunci Saya?</b><br><em>Pada halaman akun, arahkan kursor anda dan pilih Ubah Kata Kunci atau Ganti Kata Kunci</em></p>";
            list.add(faq);
        }
        {
            FaqHolder faq = new FaqHolder();
            faq.title = getActivity().getResources().getString(R.string.buying_ticket);
            faq.description = "<p>\n" +
                    "<p><b>Bagaimana cara membeli tiket di Kiostix.com? </b><br><em>Untuk pembelian tiket di kiostix.com, ikuti langkah-langkah berikut : </em></p>\n" +
                    "<p><em>Step 1 - Jelajahi acara-acara yang tercantum berdasarkan kategori acara, atau gunakan fitur \"Search\" untuk menemukan acara spesifik, klik acara tersebut untuk informasi detail.</em></p>\n" +
                    "<p><em>Step 2 - Lihat informasi acara, pilih Hari/Tanggal/Waktu acara, diikuti dengan pemilihan Kategori Tiket dan juga jumlah tiket sesuai dengan yang Anda inginkan. Klik button \"Beli Tiket\"</em></p>\n" +
                    "<p><em>Step 3 - Cek kembali dan pastikan rangkuman pemesanan tiket Anda sudah benar, diikuti dengan pemilihan Metode Pembayaran yang sesuai dengan Anda</em></p>\n" +
                    "<p><em>Step 4 - Setelah pemesanan selesai, Anda akan mendapatkan email konfirmasi yang akan dikirimkan ke email yang terdaftar pada Akun Kiostix Anda.</em></p>\n" +
                    "<p><br><b>Apakah terdapat batas waktu pemesanan atau transaksi di Kiostix?</b><br><em>Proses pemesanan tiket pada Kiostix memiliki batas waktu selama 10 menit per transaksi. Dalam proses pemesanan, kursi yang sudah dipesan atau kuota General Admission akan diblok sesuai pesanan Anda. Namun apabila Anda gagal menyelesaikan pembayaran atau melakukan mebatalan, blokir pesanan kursi atau kuota General Admission Anda akan secara otomatis dibuka kembali.</em><br>" +
                    "<br><b>Email Konfirmasi</b><br><em>Email Konfirmasi akan dikirim ke email Anda (sesuai dengan alamat email yang terdaftar pada Akun Kiostix Anda) beberapa saat setelah proses pemesanan tiket Anda selesai. Harap melakukan cek pada Inbox atau folder Junk-Mail pada email Anda. Apabila dalam 1x24 jam Anda belum menerima email konfirmasi, mohon menghubungi cs@kiostix.com.</em><br>" +
                    "<br><b>Alokasi Tempat Duduk?</b><br><em>Alokasi tempat duduk hanya muncul pada acara-acara yang menggunakan metode \"reserved seating\" atau pemesanan tempat duduk. Setelah memilih jumlah tiket, pilih lanjutkan dan pilihan tempat duduk akan muncul. Nomor tempat duduk yang Anda pilih akan tercetak pada rangkuman pemesanan Anda dan juga pada E-Voucher / Tiket Anda.</em></p>\n" +
                    "<p><em>Sebagai catatan; bahwa tidak semua acara memberikan format pemilihan Kursi. Pada acara-acara yang tidak memberikan format pemilihan kursi dengan nomor, Anda akan ditempatkan atau dialokasikan ke tempat duduk secara otomatis oleh sistem kami. &nbsp; </em></p>\n" +
                    "<p><b>Kursi dengan pemandangan terbatas.</b><br><em>Di beberapa lokasi acara, susunan atau set untuk pertunjukan seringkali mengarahkan pengunjung pada tempat duduk yang tidak memiliki visibilitas tinggi / tidak bisa melihat panggung secara penuh. Tempat duduk yang dianggap terhalang atau memiliki keterbatasan pandangan akan ditampilkan dalam peta tempat duduk yang tersedia.</em></p>\n" +
                    "</p>";
            list.add(faq);
        }
        {
            FaqHolder faq = new FaqHolder();
            faq.title = getActivity().getResources().getString(R.string.return_ticket);
            faq.description = "<p>\n" +
                    "<p><b>Metode pengambilan tiket dari Kiostix.com</b><br><em>Pada umumnya tiket dapat diambil di kantor Kiostix, ataupun di tempat acara. Namun pada beberapa acara, lokasi dan waktu penukaran tiket ditentukan secara spesifik oleh pihak penyelenggara acara.&nbsp;</em></p>\n" +
                    "<p><b>Kelengkapan dokumen untuk pengambilan tiket?</b><br><em>Pada proses pengambilan tiket yang dilakukan sendiri, mohon mencetak dan membawa lampiran berupa E-Voucher serta membawa identitas diri (KTP / SIM / Paspor) sesuai dengan yang tercantum pada akun Kiostix yang digunakan untuk pembelian. </em></p>\n" +
                    "<p><em>Apabila pengambilan tiket terpaksa diwakilkan, mohon menyertai Fotokopi identitas diri (KTP / SIM / Paspor) pembeli tiket, Identitas diri asli perwakilan pengambilan tiket, Fotokopi tampak depan kartu kredit yang digunakan dalam proses pembelian tiket, melampirkan surat kuasa yang telah di tanda tangan dengan materai Rp. 6,000,-</em></p>\n" +
                    "</p>";
            list.add(faq);
        }
        {
            FaqHolder faq = new FaqHolder();
            faq.title = getActivity().getResources().getString(R.string.payment_method);
            faq.description = "<p><b>Metode pembayaran apa saja yang dapat digunakan?</b><br><em>Saat ini Kiostix.com menerima sejumlah metode pembayaran, di antaranya yaitu :</em></p>\n" +
                    "<p><em>Kartu Kredit (Visa / Mastercard)</em></p>\n" +
                    "<p><em>Bank Transfer (Jaringan ATM Bersama)</em></p>\n" +
                    "<p><em>Gratis (Khusus untuk acara dalam kategori \"Gratis\")</em></p>\n" +
                    "<p><em>Mandiri Clickpay</em></p>\n" +
                    "<p><em>CIMB Clicks</em></p></p>";
            list.add(faq);
        }
        {
            FaqHolder faq = new FaqHolder();
            faq.title = getActivity().getResources().getString(R.string.lost_ticket);
            faq.description = "<p>\n" +
                    "<p><b>Apa yang harus saya lakukan jika Tiket saya hilang atau dicuri?</b><br><em>Untuk mendapatkan solusi layanan tiket hilang / dicuri, silahkan menghubungi layanan bantuan Kiostix di 02171793750 atau via email ke <a href=\"mailto:cs@kiostix.com,\">cs@kiostix.com</a>&nbsp;dengan menyiapkan kelengkapan informasi sebagai berikut:</em></p>\n" +
                    "<p><em>Informasi acara yang dibeli, Jumlah tiket acara yang yang dibeli, tanggal dan tempat saat melakukan pembelian, metode pembayaran dilengkapi dengan bukti pembayaran, nomor akun kiostix, Identitas diri (KTP / SIM / Paspor), Laporan Kehilangan / Kecurian dari kantor polisi setempat.</em></p>\n" +
                    "<p><em>*Penggantian tiket hanya berlaku untuk tiket \"Reserved Seating (RS)\" (Biaya, Syarat &amp; Ketentuan berlaku), dan tidak berlaku untuk tiket \"General Admission (GA)\"</em></p>\n" +
                    "</p>";
            list.add(faq);
        }
        {
            FaqHolder faq = new FaqHolder();
            faq.title = getActivity().getResources().getString(R.string.term_condition);
            faq.description = "        <p>Pelanggan yang terhormat, mohon dicatat bahwa setiap tiket yang dibeli di KiosTix &amp; kiostix.com haruslah mengikuti Syarat &amp; Ketentuan sebagai berikut:</p>\n" +
                    "<p>Semua tiket yang dijual oleh Kiostix.com (KiosTix) sebagai agen untuk dan atas nama Manajemen atau Pemilik Tempat Acara (Venue) atau Pemilik Acara (Promotor) yang bertanggung jawab atas Tiket Acara yang dijual. Semua pesanan atau pembelian bergantung pada ketersediaan, Kiostix berhak untuk menerima atau menolak pemesanan tiket karena alasan apapun. Dengan memesan atau membeli tiket berarti Anda seuju dengan ketentuan yang ada, yaitu :</p>\n" +
                    "<ol>\n" +
                    "<li>Pembeli Tiket tidak dapat disamakan dengan Pemegang Tiket. Pembeli Tiket adalah orang-orang yang membeli tiket untuk pertunjukan dan melakukan pembayaran sesuai dengan metode pembayaran tertentu. Sedangkan Pemegang Tiket adalah orang yang memegang tiket pada suatu acara dan berhak untuk memasuki acara sesuai dengan peraturan yang berlaku.</li>\n" +
                    "<li>Dalam hal/kejadian tertentu Pemegang Tiket adalah orang yang melakukan pembelian tiket (Pembeli Tiket), maka pemegang tiket wajib menunjukkan Identitas Diri yang jelas (KTP / SIM / Paspor), serta menyertai bukti pembelian yang sah apabila diperlukan.</li>\n" +
                    "<li>Apabila Pemegang Tiket bukan Pembeli Tiket, maka Pemegang Tiket harus melengkapi dan menyerahkan \"Surat Kuasa\" yang telah ditandatangani oleh Pembeli Tiket di atas Materai dan disertai dengan salinan Identitas Diri Pembeli Tiket yang sah (KTP / SIM / Paspor).</li>\n" +
                    "<li>Baik Pembeli Tiket maupun Pemegang Tiket sepenuhnya tunduk kepada semua ketentuan umum&nbsp;Kiostix.com (KiosTix) dan atau Operator Acara, Penyelenggara Acara (Promotor), Pemilik Acara, dan Tempat Acara.</li>\n" +
                    "<li>Terdapat kemungkinan bahwa Pemegang Tiket Acara dapat ditolak untuk memasuki Acara jika ditemukan / dibuktikan bahwa Pemegang Tiket tidak membeli melalui Kiostix.com (KiosTix) atau di setiap kanal distribusi dan penjualan resmi&nbsp;Kiostix.com (KiosTix).</li>\n" +
                    "<li>Pemegang Tiket setuju untuk meletakkan barang apapun yang diambil dan dilarang oleh Penyelenggara Acara atau Operator Acara yang diatur sesuai dengan peraturan yang berlaku, seperti; Makanan &amp; Minuman, Senjata Tajam / Senjata Api, Zat Ilegal Berbahaya, Alat Perekam Video, atau Barang Ilegal lainnya.</li>\n" +
                    "<li>Tiket yang telah dibeli tidak dapat ditukarkan atau dikembalikan karena alasan apapun tanpa pengecualian dan tidak dapat ditransfer atau dijual kembali.&nbsp;Kiostix.com (KiosTix) ataupun Penyelenggara Acara atau Operator Acara masing-masing berhak untuk membatalkan tiket yang telah ditransfer atau dijual kembali untuk menolak masuknya Pemegang Tiket.</li>\n" +
                    "<li>Tidak ada pengembalian berupa uang tiket dalam keadaan apapun kecuali jika dalam kondisi tertentu, seperti; Acara ditunda atau dibatalkan dan diberitahukan dalam pernyataan terbuka atau pemberitahuan penundaan Acara yang dibuat secara publik oleh Penyelenggara Acara (Promotor) atau Pemilik Acara melalui&nbsp;Kiostix.com (KiosTix) ataupun media terbuka lainnya yang bersifat resmi.</li>\n" +
                    "<li>Pembeli Tiket di bawah usia 18 tahun harus menyertai izin orang tua sebelum melakukan Pembelian Tiket / Barang Dagangan dari situs web&nbsp;Kiostix.com (KiosTix). Dengan bertransaksi di situs&nbsp;Kiostix.com (KiosTix), Anda menegaskan bahwa Anda telah setidaknya berusia 18 Tahun dan Anda memahami seluruh Syarat &amp; Ketentuan yang berlaku.</li>\n" +
                    "<li>Tiket (Thermal) yang hilang atau rusak karena kelalaian Pembeli Tiket atau Pemegang Tiket tidak dapat dicetak ulang karena alasan apapun.</li>\n" +
                    "<li>Pemegang Tiket atau Pengunjung Acara yang tidak membawa Tiket (Thermal / Kertas / Elektronik), tidak diperbolehkan memasuki Tempat Acara atau Acara dengan alasan apapun.</li>\n" +
                    "<li>Khusus untuk Tiket Elektronik (E-Ticket), jika Pemegang Tiket / Pembeli Tiket tidak membawa Tiket, maka dapat mencetak Tiket Elektronik (E-Ticket) dengan dikenakan biaya cetak senilai Rp. 20.000,- (Dua Puluh Ribu Rupiah) per transaksi (Kebijakan ini bergantung pada ketersediaan fasilitas dan sesuai dengan peraturan yang berlaku di Tempat Acara).</li>\n" +
                    "<li>Tiket hanya berlaku untuk 1 (satu) Orang dan 1 (satu) kali penggunaan sesuai dengan kategori atau kelas, waktu &amp; tanggal acara, atau dengan peraturan yang diatur oleh Penyelenggara Acara (Promotor) atau Operator Acara.</li>\n" +
                    "<li>Seluruh anggota yang sudah terdaftar di&nbsp;Kiostix.com (KiosTix) akan secara otomatis mendapatkan pemberitahuan terbaru dari&nbsp;Kiostix.com (KiosTix) melalui program Newsletter / KiosBuzz mengenai acara terbaru, informasi acara, penukaran E-Voucher, penawaran khusus, dan juga informasi penting lainnya. Anda dapat berhenti menerima pemberitahuan dari KiosBuzz kapan saja dengan meng-klik \"Unsubscribe\" atau \"Berhenti Berlangganan\" pada bagian akhir Newsletter dari&nbsp;Kiostix.com (KiosTix)</li>\n" +
                    "<li>Pemegang Tiket sepenuhnya peraturan, syarat dan ketentuan yang berlaku yang ditentukan oleh Penyelenggara Acara (Promotor), kecuali dinyatakan sesuai dengan pemberitahuan penundaan, informasi atau pernyataan publik dari Pemilik Acara maupun Penyelenggara Acara (Promotor) melalui&nbsp;Kiostix.com (KiosTix) dan juga media terbuka lainnya. Contoh&nbsp;\n" +
                    "<ol>\n" +
                    "<li>Anak-anak atau Anak-anak tanpa tiket tidak diizinkan memasuki Acara.</li>\n" +
                    "<li>Pada beberapa hal/kejadian tertentu, penundaan tidak berlaku ketika Pemegang Tiket telah diinformasikan agar dapat memasuki tempat Acara pada waktu yang ditentukan atau ketika sedang jeda istirahat berlangsung.</li>\n" +
                    "<li>Setiap Pemegang Tiket yang memasuki Acara diwajibkan untuk patuh pada peraturan, ketentuan dan kondisi yang berlaku di Tempat Acara.</li>\n" +
                    "<li>Batasan usia untuk memasuki Acara atau Tempat Acara.</li>\n" +
                    "<li>Perangkat fotografi, Perangkat Rekaman Audio ataupun Video tidak diizinkan selama Acara berlangsgung, kecuali terdapat pernyataan dari Pemilik Acara atau Penyelenggara Acara (Promotor).</li>\n" +
                    "<li>Penyelenggara Acara atau Operator Acara atau kiostix.com (KiosTix) dapat merekam, atau menggunakan foto Pemegang Tiket dalam berbagai materi tampilan, foto, grafis, video, pada situs web dalam keperluan publikasi.</li>\n" +
                    "<li>Penyelenggara Acara atau Operator atau Tempat Acara berhak untuk menolak masuk atau meminta Pemegang Tiket atau Pengunjung Acara untuk meninggalkan Acara jika ditemukan bahwa Pemegang Tiket atau Pengunjung Acara bertindak tidak sesuai dengan aturan atau tidak pantas atau menyebabkan ancaman bagi keamanan atau mengganggu ketertiban, tanpa pengembalian uang atau kompensasi apapun.</li>\n" +
                    "<li>Penyelenggara Acara atau Operator Acara berhak dan dapat menunda, membatalkan, menyela, atau menghentikan Acara, menghentikan layanan yang terkait dengan Acara, menolak akses ke Tempat Acara dengan alasan situasi berbahaya, cuaca buruk, faktor alam, permintaan dari Badan Hukum Pemerintah dan atau karena alasan penyebab lainnya yang berada diluar kendali dan kekuasaan Pemilik Acara, Penyelenggara Acara, Operator Acara maupun Tempat Acara.</li>\n" +
                    "<li>Seluruh peristiwa yang terjadi, baik semua risiko, kecelakaan, kehilangan atau hal-hal lain yang tidak diinginkan, baik sebelum, selama atau sesudah Acara / Peristiwa yang terjadi yang berada diluar kemampuan Penyelenggara Acara atau Operator Acara adalah tanggung jawab sepenuhnya dari Pemegang Tiket.</li>\n" +
                    "<li>kiostix.com (KiosTix) tidak memiliki kendali atas pemeliharaan atau pengelolaan Tempat Acara, atau Organisasi atau Manajemen Acara yang ada.</li>\n" +
                    "<li>Setiap keluhan mengenai Acara akan disampaikan kepada pihak Penyelenggara Acara (Promotor) serta setiap keluhan mengenai Tempat Acara akan diarahkan dan ditangani oleh Pemilik Tempat Acara atau Manajemen Tempat Acara.</li>\n" +
                    "<li>Pembeli Tiket, Pemegang Tiket, Penyelenggara Acara, Pemilik Acara dan Tempat Acara serta&nbsp;kiostix.com (KiosTix) sepenuhnya tunduk dan patuh pada hukum yang berlaku di daerah yurisdiksi Republik Indonesia.</li>\n" +
                    "</ol>\n" +
                    "</li>\n" +
                    "</ol>\n";
            list.add(faq);
        }
        adapter.notifyDataSetChanged();
    }
}
