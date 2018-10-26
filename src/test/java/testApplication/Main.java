package testApplication;

import com.treluxcom.metier.Panier;
import com.treluxcom.metier.Panierclient;
import com.treluxcom.service.IAdministrateurHome;
import com.treluxcom.service.IBoutiqueHome;
import com.treluxcom.service.ICaissierHome;
import com.treluxcom.service.IClientHome;
import com.treluxcom.service.IDevisHome;
import com.treluxcom.service.IEmployeHome;
import com.treluxcom.service.IFamilleHome;
import com.treluxcom.service.IGerantHome;
import com.treluxcom.service.IPanierHome;
import com.treluxcom.service.IPaniercaissierHome;
import com.treluxcom.service.IPanierclientHome;
import com.treluxcom.service.IPersonneHome;
import com.treluxcom.service.IProduitHome;
import com.treluxcom.service.IStockHome;
import com.treluxcom.service.Statistiques;
import com.treluxcom.utilitaire.GenerateCode;
import com.treluxcom.utilitaire.Reseau;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String args[]) throws IOException {

        String port = Reseau.getPort();
        String adresseIp = Reseau.getAdresseip();
        try {
            IPersonneHome iph = (IPersonneHome) java.rmi.Naming.lookup("rmi://" + adresseIp + ":" + port + "/rmiPersonneHome");
            IFamilleHome famhome = (IFamilleHome) java.rmi.Naming.lookup("rmi://" + adresseIp + ":" + port + "/rmiFamilleHome");
            IAdministrateurHome adm = (IAdministrateurHome) java.rmi.Naming.lookup("rmi://" + adresseIp + ":" + port + "/rmiAdministrateurHome");
            IEmployeHome emp = (IEmployeHome) java.rmi.Naming.lookup("rmi://" + adresseIp + ":" + port + "/rmiEmployeHome");
            IClientHome cli = (IClientHome) java.rmi.Naming.lookup("rmi://" + adresseIp + ":" + port + "/rmiClientHome");
            ICaissierHome cai = (ICaissierHome) java.rmi.Naming.lookup("rmi://" + adresseIp + ":" + port + "/rmiCaissierHome");
            IGerantHome ger = (IGerantHome) java.rmi.Naming.lookup("rmi://" + adresseIp + ":" + port + "/rmiGerantHome");
            IBoutiqueHome ex = (IBoutiqueHome) java.rmi.Naming.lookup("rmi://" + adresseIp + ":" + port + "/rmiBoutiqueHome");
            IDevisHome de = (IDevisHome) java.rmi.Naming.lookup("rmi://" + adresseIp + ":" + port + "/rmiDevisHome");
            Statistiques st = (Statistiques) java.rmi.Naming.lookup("rmi://" + adresseIp + ":" + port + "/rmiStatistiques");
            IStockHome stoHome = (IStockHome) java.rmi.Naming.lookup("rmi://" + adresseIp + ":" + port + "/rmiStockHome");
            IProduitHome prodHome = (IProduitHome) java.rmi.Naming.lookup("rmi://" + adresseIp + ":" + port + "/rmiProduitHome");
            IFamilleHome famHome = (IFamilleHome) java.rmi.Naming.lookup("rmi://" + adresseIp + ":" + port + "/rmiFamilleHome");

            IPanierHome panhome = (IPanierHome) java.rmi.Naming.lookup("rmi://" + adresseIp + ":" + port + "/rmiPanierHome");
            IPaniercaissierHome pancaishome = (IPaniercaissierHome) java.rmi.Naming.lookup("rmi://" + adresseIp + ":" + port + "/rmiPaniercaissierHome");
            IPanierclientHome panclihome = (IPanierclientHome) java.rmi.Naming.lookup("rmi://" + adresseIp + ":" + port + "/rmiPanierclientHome");

            System.out.println(prodHome.produitDispoList(famHome.findById("FAM9")).get(0).getPrixgerant().toString());
            Panier panier = new Panier();
            panier.setCodepanier(GenerateCode.clefUTC("PAN"));
            panier.setDatepanier(new Date());
            panier.setPrix(new BigDecimal(8900));

            Panierclient panierclient = new Panierclient();
            panierclient.setClient(cli.clientList().get(0));

            panierclient.setPanier(panier);
            panierclient.setAdresselivraison("Pikine Guinaw Rail");
            panierclient.setTypepaiement("Carte Bancaire");
            panierclient.setBonlivraison("chemin/" + panier.getCodepanier() + ".pdf");
            //panierclient.setProduits(produits);
            // panclihome.persist(panierclient);
            //update produits achetes insert panier corresodant

            System.out.println("Taille panier : " + Integer.toString(panhome.panierList().size()));
            System.out.println("Taille panier caissier : " + pancaishome.paniercaissierList().size());
            System.out.println("Taille panier client : " + panclihome.panierclientList().size());

            System.out.println("Nombre de fam dans sto pub: " + stoHome.familleList(stoHome.stockPublieList().get(0)).size());
            System.out.println("Nombre de prod dispo: " + stoHome.quantiteDispoEnStock(stoHome.findById("STO20180525113938722"), famhome.findById("FAM9")));
            System.out.println("Nombre produit produitVenduEnligne : " + stoHome.famillePublie(stoHome.stockPublieList()));

            // HashMap<String, List<Commande>> comStat= st.commande(ex.boutiqueList().get(0));
            /*            HashMap<String, Integer> comStat = st.statProduit(((Devis) de.devisList().get(0)).getCommande());
            System.out.println("En cours" + comStat.get("nombreProduitdispo"));
            Stock sto = new Stock("samastock");
            stoHome.persist(sto);
            sto = stoHome.findById("samastock");
            
            System.out.println(sto.getProduits().size());
            Produit prod = new Produit("PRO", famhome.familleList().get(0), sto);
            
            //boolean persist = prodHome.persist(prod);
            System.out.println(sto.getProduits().size());*/
        } catch (RemoteException ex) {
            ex.printStackTrace();
        } catch (NotBoundException | MalformedURLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*    System.out.println(adm.administrateurList().get(0).getPersonne().getPrenom());
        List<Boutique> bouts= new ArrayList(adm.administrateurList().get(0).getBoutiques());
        Iterator boutIt = bouts.iterator();
        while (boutIt.hasNext()) {
        Boutique boutElm = (Boutique) boutIt.next();
        System.out.println(boutElm.getCodeboutique());
        Iterator empIt = exh.EmployeList(boutElm.getCodeboutique()).iterator();
        while (empIt.hasNext()) {
        Personne emplElm = (Personne) empIt.next();
        System.out.println(emplElm.getCodepersonne());
        System.out.println(emplElm.getNom());
        System.out.println(emplElm.getPrenom());
        System.out.println(emplElm.getEmail());
        }
        }
        Iterator it=(adm.findById("ADM20180222144951173")).getBoutiques().iterator();
        while (it.hasNext()){
        Boutique bt= (Boutique) it.next();
        bt.getNom();
        }
         */
        //// Gerant c = new Gerant( new Employe(ex.boutiqueList().get(0) ,new Personne(GenerateCode.clefUTC("GER"), "Kizoo", "Kaza", "m"), "Gérant"));
        //homeclient.update( "categorie",  "mal" , "codepersonne" ,"CLI20180304130645874");
        ///   ger.persist(c);
        /*  System.out.println(boutElm.getCodeboutique());
        Iterator empIt = emp.employeList().iterator();
        while (empIt.hasNext()) {
        Employe emplElm = (Employe) empIt.next();
        Personne p=emplElm.getPersonne();
        System.out.println(p.getNom());
        System.out.println(p.getCodepersonne());
        System.out.println(p.getPrenom());
        System.out.println(p.getEmail());
        }
         */
 /*  Personne per=adm.administrateurList().get(0).getPersonne();
        List<Boutique> bouts=ex.boutiqueList();
        Iterator boutIt = bouts.iterator();
        while (boutIt.hasNext()) {
        Boutique boutElm = (Boutique) boutIt.next();
        System.out.println(boutElm.getCodeboutique());
        Iterator empIt = exh.EmployeList(boutElm.getCodeboutique()).iterator();
        while (empIt.hasNext()) {
        Personne emplElm = (Personne) empIt.next();
        System.out.println(emplElm.getCodepersonne());
        System.out.println(emplElm.getNom());
        System.out.println(emplElm.getPrenom());
        System.out.println(emplElm.getEmail());
        }
        }
        /*   List<Personne> personnes =iph.personneList();
        // ger.deleteById("GER20180405155524186");
        System.out.println("Nombre de personne: " + personnes.size());
        Iterator personnesIt=personnes.iterator();
        while(personnesIt.hasNext()){
        Personne personne=(Personne)personnesIt.next();
        System.out.println(personne.getPrenom()+" " + personne.getNom());
        }
        System.out.println("Suppression avec succes ");
        System.out.println("Connexion: " + (iph.connection("ada@trelux.com", "ada")).getNom());
         */
        //System.out.println("Référence " + pers.getPrenom());
        //ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
        //  ClientHome homeclient = new ClientHome();
        //Client c = new Client( new Personne(GenerateCode.clefUTC("CLI"), "Ebbe", "Astou", "m") ,"Bon" , 10 , true , null);
//          //homeclient.update( "categorie",  "mal" , "codepersonne" ,"CLI20180304130645874");
        //     homeclient.persist(c);
//       Client c = new ClientHome().findById("CLI20180304130645874");
//        System.out.println(c.getCodepersonne());
//        LivreurHome homelivreur = new LivreurHome();
//        homelivreur.deleteById("LIV20180222144951174");
//       PersonneHome homep = new PersonneHome();
//        Personne p = homep.connection("ada@treluxcom", "ada");
//        System.out.println(p.getNom() +" "+ p.getEmploye().getLivreur().getCodepersonne());
//        List<Boutique> boutiques = new BoutiqueHome().boutiqueList();
//        Set emps = boutiques.get(0).getEmployes();
//        Iterator iterator = emps.iterator();
//        while(iterator.hasNext()){
//            Employe employes = (Employe) iterator.next();
//            System.out.println(employes.getPersonne().getNom());
//        }
        /*Personne persadmin = new Personne(GenerateCode.clefUTC("ADM"), "Touré", "Modou", "m");
        Personne persger = new Personne(GenerateCode.clefUTC("GER"), "Dia", "Coumbiss", "f");
        Personne perscais = new Personne(GenerateCode.clefUTC("CAI"), "Ardo", "Anta", "f");
        Administrateur admin = new Administrateur(persadmin);
        //  new AdministrateurHome().persist(admin);
        Boutique bout = new Boutique(41, admin, "2014-12-27");
        new BoutiqueHome().persist(bout);*/
 /* Employe employe= new Employe(bout,persger,"gerant");
        Gerant ger = new Gerant(employe);
        new GerantHome().persist(ger);
        Gerantprivilege gerp=new Gerantprivilege (new GerantprivilegeId (ger.getCodepersonne(),5),ger,new Privilege (78));
        GerantprivilegeHome gph=new GerantprivilegeHome();
        gph.persist(gerp);*/
        //CalendrierService
        /*  LocalDate ld;
        java.util.GregorianCalendar calendar = new GregorianCalendar();
        Date heuredebut = calendar.getTime();
        Date heurefin = calendar.getTime();
        ;
        Employe employe= new Employe(bout,perscais,"caissier");
        new CaissierHome().persist(new Caissier(employe));
        CalendrierserviceId calserid= new CalendrierserviceId();
        calserid.setCodepersonne(perscais.getCodepersonne());
        calserid.setCodejour(2);
        calserid.setDatepriseservice(new Date());
        calserid.setHeuredebut(heuredebut);
        calserid.setHeurefin(heurefin);
        Calendrierservice calser=new Calendrierservice(calserid,employe,new Jour(1));
        new CalendrierserviceHome().persist(calser);
         */

 /*    System.out.println(adm.administrateurList().get(0).getPersonne().getPrenom());
         List<Boutique> bouts= new ArrayList(adm.administrateurList().get(0).getBoutiques());
        Iterator boutIt = bouts.iterator();
        while (boutIt.hasNext()) {
            Boutique boutElm = (Boutique) boutIt.next();
            System.out.println(boutElm.getCodeboutique());
            Iterator empIt = exh.EmployeList(boutElm.getCodeboutique()).iterator();
            while (empIt.hasNext()) {
                Personne emplElm = (Personne) empIt.next();
               
                        System.out.println(emplElm.getCodepersonne());
                        System.out.println(emplElm.getNom());
                        System.out.println(emplElm.getPrenom());                        
                        System.out.println(emplElm.getEmail());
                
                
            }
        }
        
        Iterator it=(adm.findById("ADM20180222144951173")).getBoutiques().iterator();
        while (it.hasNext()){
            Boutique bt= (Boutique) it.next();
            bt.getNom();
        }
         */
        //// Gerant c = new Gerant( new Employe(ex.boutiqueList().get(0) ,new Personne(GenerateCode.clefUTC("GER"), "Kizoo", "Kaza", "m"), "Gérant"));
        //homeclient.update( "categorie",  "mal" , "codepersonne" ,"CLI20180304130645874");
        ///   ger.persist(c);
        /*  System.out.println(boutElm.getCodeboutique());
            Iterator empIt = emp.employeList().iterator();
            while (empIt.hasNext()) {
                Employe emplElm = (Employe) empIt.next();
                Personne p=emplElm.getPersonne();
                        System.out.println(p.getNom());
                        System.out.println(p.getCodepersonne());
                        
                        System.out.println(p.getPrenom());                        
                        System.out.println(p.getEmail());               
                
            }
         */
 /*  Personne per=adm.administrateurList().get(0).getPersonne();        
        List<Boutique> bouts=ex.boutiqueList();
        Iterator boutIt = bouts.iterator();
        while (boutIt.hasNext()) {
            Boutique boutElm = (Boutique) boutIt.next();
            System.out.println(boutElm.getCodeboutique());
            Iterator empIt = exh.EmployeList(boutElm.getCodeboutique()).iterator();
            while (empIt.hasNext()) {
                Personne emplElm = (Personne) empIt.next();
               
                        System.out.println(emplElm.getCodepersonne());
                        System.out.println(emplElm.getNom());
                        System.out.println(emplElm.getPrenom());                        
                        System.out.println(emplElm.getEmail());
                
                
            }
        }
        
     /*   List<Personne> personnes =iph.personneList();
       // ger.deleteById("GER20180405155524186");
        System.out.println("Nombre de personne: " + personnes.size());
        Iterator personnesIt=personnes.iterator();
        while(personnesIt.hasNext()){
            Personne personne=(Personne)personnesIt.next();
            System.out.println(personne.getPrenom()+" " + personne.getNom());
        }
        
        
        System.out.println("Suppression avec succes ");           
        System.out.println("Connexion: " + (iph.connection("ada@trelux.com", "ada")).getNom());
        
         */
        //System.out.println("Référence " + pers.getPrenom());
        //ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml"); 
        //  ClientHome homeclient = new ClientHome();
        //Client c = new Client( new Personne(GenerateCode.clefUTC("CLI"), "Ebbe", "Astou", "m") ,"Bon" , 10 , true , null);
//          //homeclient.update( "categorie",  "mal" , "codepersonne" ,"CLI20180304130645874");
        //     homeclient.persist(c);
//       Client c = new ClientHome().findById("CLI20180304130645874");
//        System.out.println(c.getCodepersonne());
//        LivreurHome homelivreur = new LivreurHome();
//        homelivreur.deleteById("LIV20180222144951174");
//       PersonneHome homep = new PersonneHome();
//        Personne p = homep.connection("ada@treluxcom", "ada");
//        System.out.println(p.getNom() +" "+ p.getEmploye().getLivreur().getCodepersonne());
//        List<Boutique> boutiques = new BoutiqueHome().boutiqueList();
//        Set emps = boutiques.get(0).getEmployes();
//        Iterator iterator = emps.iterator();
//        while(iterator.hasNext()){
//            Employe employes = (Employe) iterator.next();
//            System.out.println(employes.getPersonne().getNom());
//        }
        /*Personne persadmin = new Personne(GenerateCode.clefUTC("ADM"), "Touré", "Modou", "m");
 Personne persger = new Personne(GenerateCode.clefUTC("GER"), "Dia", "Coumbiss", "f");
 Personne perscais = new Personne(GenerateCode.clefUTC("CAI"), "Ardo", "Anta", "f");

 Administrateur admin = new Administrateur(persadmin);
  //  new AdministrateurHome().persist(admin);
 Boutique bout = new Boutique(41, admin, "2014-12-27");
   new BoutiqueHome().persist(bout);*/
 /* Employe employe= new Employe(bout,persger,"gerant");
 Gerant ger = new Gerant(employe);
    new GerantHome().persist(ger);
    Gerantprivilege gerp=new Gerantprivilege (new GerantprivilegeId (ger.getCodepersonne(),5),ger,new Privilege (78));
    GerantprivilegeHome gph=new GerantprivilegeHome();
    gph.persist(gerp);*/
        //CalendrierService 

        /*  LocalDate ld;
    java.util.GregorianCalendar calendar = new GregorianCalendar();				 
    Date heuredebut = calendar.getTime();
    Date heurefin = calendar.getTime();
;
    Employe employe= new Employe(bout,perscais,"caissier");
        new CaissierHome().persist(new Caissier(employe));
    CalendrierserviceId calserid= new CalendrierserviceId();
    calserid.setCodepersonne(perscais.getCodepersonne());
    calserid.setCodejour(2);
    calserid.setDatepriseservice(new Date()); 
    calserid.setHeuredebut(heuredebut);
    calserid.setHeurefin(heurefin);
    Calendrierservice calser=new Calendrierservice(calserid,employe,new Jour(1));
    new CalendrierserviceHome().persist(calser);
         */
    }
}
