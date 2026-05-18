package org.bdj;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.MediaTracker;
import java.io.File;
import java.util.ArrayList;

public class Screen extends Container
{
    private static final long serialVersionUID = 4761178503523947426L;
    private ArrayList messages;
    private Font font;
    private Image background;
    private Image logo;
    
    // Posisi Y awal untuk tulisan (mulai dari agak tengah ke atas)
    public int top = 250; 

    public Screen(ArrayList messages)
    {
        this.messages = messages;
        
        // --- 1. LOAD FONT CUSTOM ---
        try {
            File fontFile = new File("BDMV/AUXDATA/00000.otf");
            font = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(Font.BOLD, 36f);
        } catch (Exception e) {
            font = new Font(null, Font.BOLD, 36);
        }
        
        // --- 2. LOAD GAMBAR BACKGROUND DENGAN MEDIATRACKER (Ala RajabbTechs) ---
        try {
            background = Toolkit.getDefaultToolkit().getImage("/disc/BDMV/AUXDATA/bg.png"); // Path ngikutin referensi
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(background, 0);
            tracker.waitForAll(); // Nunggu gambar beres di-load
        } catch (Exception e) {
            background = null;
        }

        // --- 3. LOAD LOGO ---
        try {
            logo = Toolkit.getDefaultToolkit().getImage("/disc/BDMV/META/DL/logo.png");
            MediaTracker logoTracker = new MediaTracker(this);
            logoTracker.addImage(logo, 0);
            logoTracker.waitForAll();
        } catch (Exception e) {
            logo = null;
        }
    }
    
    public void paint(Graphics g)
    {
        // --- GAMBAR BACKGROUND UTAMA ---
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(new Color(15, 15, 15)); // Abu-abu sangat gelap
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // --- BIKIN KOTAK SEMI-TRANSPARAN DI TENGAH LAYAR ---
        // Ukuran kotak diset lebar 1400px, tinggi 700px (Bisa lu sesuaikan)
        int boxW = 1400;
        int boxH = 700;
        int boxX = (getWidth() - boxW) / 2; // Rata tengah horizontal
        int boxY = (getHeight() - boxH) / 2; // Rata tengah vertikal

        // Isi warna kotak (Hitam dengan opacity/alpha 180 dari 255)
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRoundRect(boxX, boxY, boxW, boxH, 30, 30); // 30 itu tingkat kelengkungan sudut

        // Bikin garis pinggir (border) untuk kotak
        g.setColor(new Color(100, 100, 100)); // Abu-abu
        g.drawRoundRect(boxX, boxY, boxW, boxH, 30, 30);
        
        // --- GAMBAR LOGO (Di dalam atau di atas kotak) ---
        if (logo != null) {
            // Taruh logo di pojok kanan atas LAYAR (bukan kotak)
            g.drawImage(logo, getWidth() - 250, 50, 180, 180, this); 
        }

        // --- SETTING FONT ---
        g.setFont(font);
        
        // --- LOOPING TEKS (RATA TENGAH) ---
        for(int i = 0; i < messages.size(); i++)
        {
            String message = (String)messages.get(i);
            
            // Hitung panjang teks biar bisa ditaruh persis di tengah layar
            int message_width = g.getFontMetrics().stringWidth(message);
            int textX = (getWidth() - message_width) / 2;
            int textY = top + (i * 50); // Jarak antar baris 50px
            
            // (Opsional) Bikin bayangan teks (Drop Shadow) biar makin tegas
            g.setColor(Color.BLACK);
            g.drawString(message, textX + 2, textY + 2);
            
            // Teks Asli (Warna Kuning / Putih)
            // Kasih kondisi, baris pertama beda warna biar kaya judul
            if (i == 0 || i == 1) {
                g.setColor(Color.WHITE); // Baris atas warna putih
            } else {
                g.setColor(Color.YELLOW); // Sisanya kuning
            }
            
            g.drawString(message, textX, textY);
        }
    }
}
