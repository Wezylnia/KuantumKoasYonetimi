import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

// ==================== MAIN PROGRAM (İsmi 'Main' yapıldı) ====================
public class Main {
    private static List<KuantumNesnesi> envanter = new ArrayList<>();
    private static Random random = new Random();
    private static int nesneCounter = 1;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║      OMEGA SEKTÖRÜ - KUANTUM VERİ AMBARI                 ║");
        System.out.println("║      Hoş geldiniz, Vardiya Amiri!                        ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");

        try {
            while (true) {
                menuGoster();
                // Scanner hatasını önlemek için hasNext kontrolü eklenebilir
                if (!scanner.hasNextLine()) break; 
                String secim = scanner.nextLine().trim();

                switch (secim) {
                    case "1":
                        yeniNesneEkle();
                        break;
                    case "2":
                        envanteriListele();
                        break;
                    case "3":
                        nesneAnalizEt();
                        break;
                    case "4":
                        acilSogutmaYap();
                        break;
                    case "5":
                        System.out.println("\nVardiya sona erdi. Güle güle!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("\n❌ Geçersiz seçim! Lütfen 1-5 arası bir sayı girin.");
                        break;
                }
            }
        } catch (KuantumCokusuException ex) {
            System.out.println("\n╔══════════════════════════════════════════════════════════╗");
            System.out.println("║   💥💥💥 SİSTEM ÇÖKTÜ! TAHLİYE BAŞLATILIYOR... 💥💥💥    ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝");
            System.out.println("\n" + ex.getMessage());
            System.out.println("\n[GAME OVER]");
        }
    }

    private static void menuGoster() {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("    KUANTUM AMBARI KONTROL PANELİ");
        System.out.println("════════════════════════════════════════");
        System.out.println("1. Yeni Nesne Ekle");
        System.out.println("2. Tüm Envanteri Listele (Durum Raporu)");
        System.out.println("3. Nesneyi Analiz Et");
        System.out.println("4. Acil Durum Soğutması Yap");
        System.out.println("5. Çıkış");
        System.out.println("════════════════════════════════════════");
        System.out.print("Seçiminiz: ");
    }

    private static void yeniNesneEkle() {
        int tip = random.nextInt(3) + 1; // 1, 2 veya 3
        double stabilite = random.nextInt(51) + 50; // 50-100 arası
        String id = String.format("QN-%04d", nesneCounter++);

        KuantumNesnesi yeniNesne;

        switch (tip) {
            case 1:
                yeniNesne = new VeriPaketi(id, stabilite);
                System.out.printf("\n✅ Yeni VeriPaketi eklendi: %s (Stabilite: %%%.0f)%n", id, stabilite);
                break;
            case 2:
                yeniNesne = new KaranlikMadde(id, stabilite);
                System.out.printf("\n⚠️ Yeni KaranlıkMadde eklendi: %s (Stabilite: %%%.0f)%n", id, stabilite);
                break;
            default:
                yeniNesne = new AntiMadde(id, stabilite);
                System.out.printf("\n🔴 Yeni AntiMadde eklendi: %s (Stabilite: %%%.0f)%n", id, stabilite);
                break;
        }

        envanter.add(yeniNesne);
    }

    private static void envanteriListele() {
        if (envanter.isEmpty()) {
            System.out.println("\n📦 Envanter boş. Henüz nesne eklenmedi.");
            return;
        }

        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                     ENVANTER RAPORU                      ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");

        for (KuantumNesnesi nesne : envanter) {
            System.out.println(nesne.durumBilgisi());
        }
    }

    private static void nesneAnalizEt() throws KuantumCokusuException {
        if (envanter.isEmpty()) {
            System.out.println("\n📦 Envanter boş. Analiz edilecek nesne yok.");
            return;
        }

        System.out.print("\nAnaliz edilecek nesnenin ID'sini girin: ");
        if (!scanner.hasNextLine()) return;
        String id = scanner.nextLine().trim();

        KuantumNesnesi nesne = null;
        for (KuantumNesnesi n : envanter) {
            if (n.getId().equalsIgnoreCase(id)) {
                nesne = n;
                break;
            }
        }

        if (nesne == null) {
            System.out.printf("\n❌ '%s' ID'li nesne bulunamadı!%n", id);
            return;
        }

        System.out.printf("\n🔬 %s analiz ediliyor...%n", id);
        nesne.analizEt();
        System.out.printf("Analiz tamamlandı. Yeni stabilite: %%%.1f%n", nesne.getStabilite());
    }

    private static void acilSogutmaYap() {
        if (envanter.isEmpty()) {
            System.out.println("\n📦 Envanter boş. Soğutulacak nesne yok.");
            return;
        }

        System.out.print("\nSoğutulacak nesnenin ID'sini girin: ");
        if (!scanner.hasNextLine()) return;
        String id = scanner.nextLine().trim();

        KuantumNesnesi nesne = null;
        for (KuantumNesnesi n : envanter) {
            if (n.getId().equalsIgnoreCase(id)) {
                nesne = n;
                break;
            }
        }

        if (nesne == null) {
            System.out.printf("\n❌ '%s' ID'li nesne bulunamadı!%n", id);
            return;
        }

        // Type checking with instanceof
        if (nesne instanceof IKritik) {
            ((IKritik) nesne).acilDurumSogutmasi();
        } else {
            System.out.printf("\n❌ Bu nesne soğutulamaz! '%s' kritik bir nesne değil.%n", id);
        }
    }
}

// ==================== CUSTOM EXCEPTION ====================
class KuantumCokusuException extends Exception {
    private String nesneId;

    public KuantumCokusuException(String nesneId) {
        super("KUANTUM ÇÖKÜŞÜ! Nesne ID: " + nesneId + " patladı!");
        this.nesneId = nesneId;
    }

    public String getNesneId() {
        return nesneId;
    }
}

// ==================== INTERFACE ====================
interface IKritik {
    void acilDurumSogutmasi();
}

// ==================== ABSTRACT CLASS ====================
abstract class KuantumNesnesi {
    protected String id;
    private double stabilite;
    private int tehlikeSeviyesi;

    public KuantumNesnesi(String id, double stabilite, int tehlikeSeviyesi) {
        this.id = id;
        setStabilite(stabilite);
        setTehlikeSeviyesi(tehlikeSeviyesi);
    }

    public String getId() {
        return id;
    }

    public double getStabilite() {
        return stabilite;
    }

    public void setStabilite(double stabilite) {
        if (stabilite > 100) {
            this.stabilite = 100;
        } else {
            this.stabilite = stabilite; // 0'ın altına düşebilir, exception fırlatılacak
        }
    }

    public int getTehlikeSeviyesi() {
        return tehlikeSeviyesi;
    }

    public void setTehlikeSeviyesi(int tehlikeSeviyesi) {
        if (tehlikeSeviyesi < 1) {
            this.tehlikeSeviyesi = 1;
        } else if (tehlikeSeviyesi > 10) {
            this.tehlikeSeviyesi = 10;
        } else {
            this.tehlikeSeviyesi = tehlikeSeviyesi;
        }
    }

    public abstract void analizEt() throws KuantumCokusuException;

    public String durumBilgisi() {
        return String.format("[%s] Stabilite: %%%.1f | Tehlike: %d/10", id, stabilite, tehlikeSeviyesi);
    }

    protected void stabiliteKontrol() throws KuantumCokusuException {
        if (stabilite <= 0) {
            throw new KuantumCokusuException(id);
        }
    }
}

// ==================== CONCRETE CLASSES ====================
class VeriPaketi extends KuantumNesnesi {
    public VeriPaketi(String id, double stabilite) {
        super(id, stabilite, 2);
    }

    @Override
    public void analizEt() throws KuantumCokusuException {
        System.out.println("Veri içeriği okundu.");
        setStabilite(getStabilite() - 5);
        stabiliteKontrol();
    }

    @Override
    public String durumBilgisi() {
        return super.durumBilgisi() + " [VeriPaketi - Güvenli]";
    }
}

class KaranlikMadde extends KuantumNesnesi implements IKritik {
    public KaranlikMadde(String id, double stabilite) {
        super(id, stabilite, 7);
    }

    @Override
    public void analizEt() throws KuantumCokusuException {
        System.out.println("Karanlık madde analiz ediliyor... Dikkatli olun!");
        setStabilite(getStabilite() - 15);
        stabiliteKontrol();
    }

    @Override
    public void acilDurumSogutmasi() {
        setStabilite(getStabilite() + 50);
        System.out.printf("[%s] Acil soğutma uygulandı! Yeni stabilite: %%%.1f%n", getId(), getStabilite());
    }

    @Override
    public String durumBilgisi() {
        return super.durumBilgisi() + " [KaranlıkMadde - TEHLİKELİ!]";
    }
}

class AntiMadde extends KuantumNesnesi implements IKritik {
    public AntiMadde(String id, double stabilite) {
        super(id, stabilite, 10);
    }

    @Override
    public void analizEt() throws KuantumCokusuException {
        System.out.println("⚠️ EVRENİN DOKUSU TİTRİYOR... ⚠️");
        setStabilite(getStabilite() - 25);
        stabiliteKontrol();
    }

    @Override
    public void acilDurumSogutmasi() {
        setStabilite(getStabilite() + 50);
        System.out.printf("[%s] ACİL soğutma uygulandı! Yeni stabilite: %%%.1f%n", getId(), getStabilite());
    }

    @Override
    public String durumBilgisi() {
        return super.durumBilgisi() + " [AntiMadde - ÇOK TEHLİKELİ!!!]";
    }
}