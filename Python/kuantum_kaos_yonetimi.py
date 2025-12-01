import random
from abc import ABC, abstractmethod
from typing import List, Optional


# ==================== CUSTOM EXCEPTION ====================
class KuantumCokusuException(Exception):
    """Kuantum çöküşü gerçekleştiğinde fırlatılan özel hata sınıfı."""
    
    def __init__(self, nesne_id: str):
        self.nesne_id = nesne_id
        super().__init__(f"KUANTUM ÇÖKÜŞÜ! Nesne ID: {nesne_id} patladı!")


# ==================== INTERFACE (Abstract Base Class in Python) ====================
class IKritik(ABC):
    """Kritik nesneler için arayüz."""
    
    @abstractmethod
    def acil_durum_sogutmasi(self) -> None:
        """Acil durum soğutması uygular, stabiliteyi +50 artırır."""
        pass


# ==================== ABSTRACT CLASS ====================
class KuantumNesnesi(ABC):
    """Tüm kuantum nesnelerinin temel sınıfı."""
    
    def __init__(self, nesne_id: str, stabilite: float, tehlike_seviyesi: int):
        self._id = nesne_id
        self._stabilite = 0.0
        self._tehlike_seviyesi = 1
        self.stabilite = stabilite
        self.tehlike_seviyesi = tehlike_seviyesi
    
    @property
    def id(self) -> str:
        return self._id
    
    @property
    def stabilite(self) -> float:
        return self._stabilite
    
    @stabilite.setter
    def stabilite(self, value: float) -> None:
        if value > 100:
            self._stabilite = 100
        else:
            self._stabilite = value  # 0'ın altına düşebilir, exception fırlatılacak
    
    @property
    def tehlike_seviyesi(self) -> int:
        return self._tehlike_seviyesi
    
    @tehlike_seviyesi.setter
    def tehlike_seviyesi(self, value: int) -> None:
        if value < 1:
            self._tehlike_seviyesi = 1
        elif value > 10:
            self._tehlike_seviyesi = 10
        else:
            self._tehlike_seviyesi = value
    
    @abstractmethod
    def analiz_et(self) -> None:
        """Nesneyi analiz eder. Alt sınıflar bu metodu implement etmelidir."""
        pass
    
    def durum_bilgisi(self) -> str:
        """Nesnenin durum bilgisini döndürür."""
        return f"[{self._id}] Stabilite: %{self._stabilite:.1f} | Tehlike: {self._tehlike_seviyesi}/10"
    
    def _stabilite_kontrol(self) -> None:
        """Stabilite kontrolü yapar, 0 veya altındaysa exception fırlatır."""
        if self._stabilite <= 0:
            raise KuantumCokusuException(self._id)


# ==================== CONCRETE CLASSES ====================
class VeriPaketi(KuantumNesnesi):
    """Güvenli veri paketi sınıfı."""
    
    def __init__(self, nesne_id: str, stabilite: float):
        super().__init__(nesne_id, stabilite, 2)
    
    def analiz_et(self) -> None:
        print("Veri içeriği okundu.")
        self.stabilite -= 5
        self._stabilite_kontrol()
    
    def durum_bilgisi(self) -> str:
        return super().durum_bilgisi() + " [VeriPaketi - Güvenli]"


class KaranlikMadde(KuantumNesnesi, IKritik):
    """Tehlikeli karanlık madde sınıfı."""
    
    def __init__(self, nesne_id: str, stabilite: float):
        super().__init__(nesne_id, stabilite, 7)
    
    def analiz_et(self) -> None:
        print("Karanlık madde analiz ediliyor... Dikkatli olun!")
        self.stabilite -= 15
        self._stabilite_kontrol()
    
    def acil_durum_sogutmasi(self) -> None:
        self.stabilite += 50
        print(f"[{self.id}] Acil soğutma uygulandı! Yeni stabilite: %{self.stabilite:.1f}")
    
    def durum_bilgisi(self) -> str:
        return super().durum_bilgisi() + " [KaranlıkMadde - TEHLİKELİ!]"


class AntiMadde(KuantumNesnesi, IKritik):
    """Çok tehlikeli anti madde sınıfı."""
    
    def __init__(self, nesne_id: str, stabilite: float):
        super().__init__(nesne_id, stabilite, 10)
    
    def analiz_et(self) -> None:
        print("⚠️ EVRENİN DOKUSU TİTRİYOR... ⚠️")
        self.stabilite -= 25
        self._stabilite_kontrol()
    
    def acil_durum_sogutmasi(self) -> None:
        self.stabilite += 50
        print(f"[{self.id}] ACİL soğutma uygulandı! Yeni stabilite: %{self.stabilite:.1f}")
    
    def durum_bilgisi(self) -> str:
        return super().durum_bilgisi() + " [AntiMadde - ÇOK TEHLİKELİ!!!]"


# ==================== MAIN PROGRAM ====================
class KuantumAmbarYonetimi:
    """Ana uygulama sınıfı."""
    
    def __init__(self):
        self.envanter: List[KuantumNesnesi] = []
        self.nesne_counter = 1
    
    def menu_goster(self) -> None:
        print("\n════════════════════════════════════════")
        print("    KUANTUM AMBARI KONTROL PANELİ")
        print("════════════════════════════════════════")
        print("1. Yeni Nesne Ekle")
        print("2. Tüm Envanteri Listele (Durum Raporu)")
        print("3. Nesneyi Analiz Et")
        print("4. Acil Durum Soğutması Yap")
        print("5. Çıkış")
        print("════════════════════════════════════════")
    
    def yeni_nesne_ekle(self) -> None:
        tip = random.randint(1, 3)
        stabilite = random.randint(50, 100)
        nesne_id = f"QN-{self.nesne_counter:04d}"
        self.nesne_counter += 1
        
        if tip == 1:
            yeni_nesne = VeriPaketi(nesne_id, stabilite)
            print(f"\n✅ Yeni VeriPaketi eklendi: {nesne_id} (Stabilite: %{stabilite})")
        elif tip == 2:
            yeni_nesne = KaranlikMadde(nesne_id, stabilite)
            print(f"\n⚠️ Yeni KaranlıkMadde eklendi: {nesne_id} (Stabilite: %{stabilite})")
        else:
            yeni_nesne = AntiMadde(nesne_id, stabilite)
            print(f"\n🔴 Yeni AntiMadde eklendi: {nesne_id} (Stabilite: %{stabilite})")
        
        self.envanter.append(yeni_nesne)
    
    def envanteri_listele(self) -> None:
        if not self.envanter:
            print("\n📦 Envanter boş. Henüz nesne eklenmedi.")
            return
        
        print("\n╔══════════════════════════════════════════════════════════╗")
        print("║                    ENVANTER RAPORU                        ║")
        print("╚══════════════════════════════════════════════════════════╝")
        
        for nesne in self.envanter:
            print(nesne.durum_bilgisi())
    
    def nesne_bul(self, nesne_id: str) -> Optional[KuantumNesnesi]:
        for nesne in self.envanter:
            if nesne.id.lower() == nesne_id.lower():
                return nesne
        return None
    
    def nesne_analiz_et(self) -> None:
        if not self.envanter:
            print("\n📦 Envanter boş. Analiz edilecek nesne yok.")
            return
        
        nesne_id = input("\nAnaliz edilecek nesnenin ID'sini girin: ").strip()
        nesne = self.nesne_bul(nesne_id)
        
        if nesne is None:
            print(f"\n❌ '{nesne_id}' ID'li nesne bulunamadı!")
            return
        
        print(f"\n🔬 {nesne_id} analiz ediliyor...")
        nesne.analiz_et()
        print(f"Analiz tamamlandı. Yeni stabilite: %{nesne.stabilite:.1f}")
    
    def acil_sogutma_yap(self) -> None:
        if not self.envanter:
            print("\n📦 Envanter boş. Soğutulacak nesne yok.")
            return
        
        nesne_id = input("\nSoğutulacak nesnenin ID'sini girin: ").strip()
        nesne = self.nesne_bul(nesne_id)
        
        if nesne is None:
            print(f"\n❌ '{nesne_id}' ID'li nesne bulunamadı!")
            return
        
        # Type checking with isinstance
        if isinstance(nesne, IKritik):
            nesne.acil_durum_sogutmasi()
        else:
            print(f"\n❌ Bu nesne soğutulamaz! '{nesne_id}' kritik bir nesne değil.")
    
    def calistir(self) -> None:
        print("╔══════════════════════════════════════════════════════════╗")
        print("║     OMEGA SEKTÖRÜ - KUANTUM VERİ AMBARI                  ║")
        print("║     Hoş geldiniz, Vardiya Amiri!                         ║")
        print("╚══════════════════════════════════════════════════════════╝")
        
        try:
            while True:
                self.menu_goster()
                secim = input("Seçiminiz: ").strip()
                
                if secim == "1":
                    self.yeni_nesne_ekle()
                elif secim == "2":
                    self.envanteri_listele()
                elif secim == "3":
                    self.nesne_analiz_et()
                elif secim == "4":
                    self.acil_sogutma_yap()
                elif secim == "5":
                    print("\nVardiya sona erdi. Güle güle!")
                    break
                else:
                    print("\n❌ Geçersiz seçim! Lütfen 1-5 arası bir sayı girin.")
        
        except KuantumCokusuException as ex:
            print("\n╔══════════════════════════════════════════════════════════╗")
            print("║  💥💥💥 SİSTEM ÇÖKTÜ! TAHLİYE BAŞLATILIYOR... 💥💥💥    ║")
            print("╚══════════════════════════════════════════════════════════╝")
            print(f"\n{ex}")
            print("\n[GAME OVER]")


# ==================== ENTRY POINT ====================
if __name__ == "__main__":
    uygulama = KuantumAmbarYonetimi()
    uygulama.calistir()