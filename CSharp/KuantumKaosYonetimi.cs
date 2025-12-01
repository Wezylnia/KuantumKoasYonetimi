using System;
using System.Collections.Generic;

namespace KuantumKaosYonetimi
{
    // ==================== CUSTOM EXCEPTION ====================
    public class KuantumCokusuException : Exception
    {
        public string NesneID { get; }

        public KuantumCokusuException(string nesneId)
            : base($"KUANTUM ÇÖKÜŞÜ! Nesne ID: {nesneId} patladı!")
        {
            NesneID = nesneId;
        }
    }

    // ==================== INTERFACE ====================
    public interface IKritik
    {
        void AcilDurumSogutmasi();
    }

    // ==================== ABSTRACT CLASS ====================
    public abstract class KuantumNesnesi
    {
        public string ID { get; protected set; }

        private double _stabilite;
        public double Stabilite
        {
            get => _stabilite;
            set
            {
                if (value > 100)
                    _stabilite = 100;
                else if (value < 0)
                    _stabilite = value; // 0'ın altına düşebilir, exception fırlatılacak
                else
                    _stabilite = value;
            }
        }

        private int _tehlikeSeviyesi;
        public int TehlikeSeviyesi
        {
            get => _tehlikeSeviyesi;
            set
            {
                if (value < 1) _tehlikeSeviyesi = 1;
                else if (value > 10) _tehlikeSeviyesi = 10;
                else _tehlikeSeviyesi = value;
            }
        }

        protected KuantumNesnesi(string id, double stabilite, int tehlikeSeviyesi)
        {
            ID = id;
            Stabilite = stabilite;
            TehlikeSeviyesi = tehlikeSeviyesi;
        }

        public abstract void AnalizEt();

        public virtual string DurumBilgisi()
        {
            return $"[{ID}] Stabilite: %{Stabilite:F1} | Tehlike: {TehlikeSeviyesi}/10";
        }

        protected void StabiliteKontrol()
        {
            if (Stabilite <= 0)
            {
                throw new KuantumCokusuException(ID);
            }
        }
    }

    // ==================== CONCRETE CLASSES ====================
    public class VeriPaketi : KuantumNesnesi
    {
        public VeriPaketi(string id, double stabilite)
            : base(id, stabilite, 2) // Düşük tehlike seviyesi
        {
        }

        public override void AnalizEt()
        {
            Console.WriteLine("Veri içeriği okundu.");
            Stabilite -= 5;
            StabiliteKontrol();
        }

        public override string DurumBilgisi()
        {
            return base.DurumBilgisi() + " [VeriPaketi - Güvenli]";
        }
    }

    public class KaranlikMadde : KuantumNesnesi, IKritik
    {
        public KaranlikMadde(string id, double stabilite)
            : base(id, stabilite, 7) // Yüksek tehlike seviyesi
        {
        }

        public override void AnalizEt()
        {
            Console.WriteLine("Karanlık madde analiz ediliyor... Dikkatli olun!");
            Stabilite -= 15;
            StabiliteKontrol();
        }

        public void AcilDurumSogutmasi()
        {
            Stabilite += 50;
            Console.WriteLine($"[{ID}] Acil soğutma uygulandı! Yeni stabilite: %{Stabilite:F1}");
        }

        public override string DurumBilgisi()
        {
            return base.DurumBilgisi() + " [KaranlıkMadde - TEHLİKELİ!]";
        }
    }

    public class AntiMadde : KuantumNesnesi, IKritik
    {
        public AntiMadde(string id, double stabilite)
            : base(id, stabilite, 10) // Maximum tehlike seviyesi
        {
        }

        public override void AnalizEt()
        {
            Console.WriteLine("⚠️ EVRENİN DOKUSU TİTRİYOR... ⚠️");
            Stabilite -= 25;
            StabiliteKontrol();
        }

        public void AcilDurumSogutmasi()
        {
            Stabilite += 50;
            Console.WriteLine($"[{ID}] ACİL soğutma uygulandı! Yeni stabilite: %{Stabilite:F1}");
        }

        public override string DurumBilgisi()
        {
            return base.DurumBilgisi() + " [AntiMadde - ÇOK TEHLİKELİ!!!]";
        }
    }

    // ==================== MAIN PROGRAM ====================
    class Program
    {
        static List<KuantumNesnesi> envanter = new List<KuantumNesnesi>();
        static Random random = new Random();
        static int nesneCounter = 1;

        static void Main(string[] args)
        {
            Console.OutputEncoding = System.Text.Encoding.UTF8;
            Console.WriteLine("╔══════════════════════════════════════════════════════════╗");
            Console.WriteLine("║     OMEGA SEKTÖRÜ - KUANTUM VERİ AMBARI                  ║");
            Console.WriteLine("║     Hoş geldiniz, Vardiya Amiri!                         ║");
            Console.WriteLine("╚══════════════════════════════════════════════════════════╝");

            try
            {
                while (true)
                {
                    MenuGoster();
                    string secim = Console.ReadLine();

                    switch (secim)
                    {
                        case "1":
                            YeniNesneEkle();
                            break;
                        case "2":
                            EnvanteriListele();
                            break;
                        case "3":
                            NesneAnalizEt();
                            break;
                        case "4":
                            AcilSogutmaYap();
                            break;
                        case "5":
                            Console.WriteLine("\nVardiya sona erdi. Güle güle!");
                            return;
                        default:
                            Console.WriteLine("\n❌ Geçersiz seçim! Lütfen 1-5 arası bir sayı girin.");
                            break;
                    }
                }
            }
            catch (KuantumCokusuException ex)
            {
                Console.WriteLine("\n╔══════════════════════════════════════════════════════════╗");
                Console.WriteLine("║  💥💥💥 SİSTEM ÇÖKTÜ! TAHLİYE BAŞLATILIYOR... 💥💥💥    ║");
                Console.WriteLine("╚══════════════════════════════════════════════════════════╝");
                Console.WriteLine($"\n{ex.Message}");
                Console.WriteLine("\n[GAME OVER]");
            }
        }

        static void MenuGoster()
        {
            Console.WriteLine("\n════════════════════════════════════════");
            Console.WriteLine("    KUANTUM AMBARI KONTROL PANELİ");
            Console.WriteLine("════════════════════════════════════════");
            Console.WriteLine("1. Yeni Nesne Ekle");
            Console.WriteLine("2. Tüm Envanteri Listele (Durum Raporu)");
            Console.WriteLine("3. Nesneyi Analiz Et");
            Console.WriteLine("4. Acil Durum Soğutması Yap");
            Console.WriteLine("5. Çıkış");
            Console.WriteLine("════════════════════════════════════════");
            Console.Write("Seçiminiz: ");
        }

        static void YeniNesneEkle()
        {
            int tip = random.Next(1, 4); // 1, 2 veya 3
            double stabilite = random.Next(50, 101); // 50-100 arası başlangıç stabilitesi
            string id = $"QN-{nesneCounter++:D4}";

            KuantumNesnesi yeniNesne;

            switch (tip)
            {
                case 1:
                    yeniNesne = new VeriPaketi(id, stabilite);
                    Console.WriteLine($"\n✅ Yeni VeriPaketi eklendi: {id} (Stabilite: %{stabilite})");
                    break;
                case 2:
                    yeniNesne = new KaranlikMadde(id, stabilite);
                    Console.WriteLine($"\n⚠️ Yeni KaranlıkMadde eklendi: {id} (Stabilite: %{stabilite})");
                    break;
                default:
                    yeniNesne = new AntiMadde(id, stabilite);
                    Console.WriteLine($"\n🔴 Yeni AntiMadde eklendi: {id} (Stabilite: %{stabilite})");
                    break;
            }

            envanter.Add(yeniNesne);
        }

        static void EnvanteriListele()
        {
            if (envanter.Count == 0)
            {
                Console.WriteLine("\n📦 Envanter boş. Henüz nesne eklenmedi.");
                return;
            }

            Console.WriteLine("\n╔══════════════════════════════════════════════════════════╗");
            Console.WriteLine("║                    ENVANTER RAPORU                        ║");
            Console.WriteLine("╚══════════════════════════════════════════════════════════╝");

            foreach (var nesne in envanter)
            {
                Console.WriteLine(nesne.DurumBilgisi());
            }
        }

        static void NesneAnalizEt()
        {
            if (envanter.Count == 0)
            {
                Console.WriteLine("\n📦 Envanter boş. Analiz edilecek nesne yok.");
                return;
            }

            Console.Write("\nAnaliz edilecek nesnenin ID'sini girin: ");
            string id = Console.ReadLine();

            KuantumNesnesi nesne = envanter.Find(n => n.ID.Equals(id, StringComparison.OrdinalIgnoreCase));

            if (nesne == null)
            {
                Console.WriteLine($"\n❌ '{id}' ID'li nesne bulunamadı!");
                return;
            }

            Console.WriteLine($"\n🔬 {id} analiz ediliyor...");
            nesne.AnalizEt();
            Console.WriteLine($"Analiz tamamlandı. Yeni stabilite: %{nesne.Stabilite:F1}");
        }

        static void AcilSogutmaYap()
        {
            if (envanter.Count == 0)
            {
                Console.WriteLine("\n📦 Envanter boş. Soğutulacak nesne yok.");
                return;
            }

            Console.Write("\nSoğutulacak nesnenin ID'sini girin: ");
            string id = Console.ReadLine();

            KuantumNesnesi nesne = envanter.Find(n => n.ID.Equals(id, StringComparison.OrdinalIgnoreCase));

            if (nesne == null)
            {
                Console.WriteLine($"\n❌ '{id}' ID'li nesne bulunamadı!");
                return;
            }

            // Type checking with 'is' keyword
            if (nesne is IKritik kritikNesne)
            {
                kritikNesne.AcilDurumSogutmasi();
            }
            else
            {
                Console.WriteLine($"\n❌ Bu nesne soğutulamaz! '{id}' kritik bir nesne değil.");
            }
        }
    }
}
