import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;


class Picture
{
	int sizeX;
	int sizeY;
	int pictureMap[][] = new int[sizeX][sizeY];
	int maxGreyLevel = 255;
	String comment;
	File tmp;
	String convertedImage;

}



public class ImageConvert extends Picture
{
    void RemoveTmp()
    {
        try
        {
            Runtime.getRuntime().exec("rm tmp.pgm  /Pulpit/Java/przetwarzanie_obrazow/");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    void CreateTmp() 
    {
        this.tmp = new File("tmp.pgm");
        try 
        {
            this.tmp.createNewFile();
        }
        catch (IOException ex) 
        {
            System.out.println("An error occurred ");
            ex.printStackTrace();
        }
    }
    
    void Display(String s) 
    {
        if (s.equals("tmp.pgm")) 
        {
            this.RemoveTmp();
            try 
            {
                Thread.sleep(50L);
            }
            catch (InterruptedException ex) 
            {
                ex.printStackTrace();
            }
            this.CreateTmp();
            this.WriteToFile(s);
            try 
            {
                Runtime.getRuntime().exec("eog tmp.pgm /Pulpit/Java/przetwarzanie_obrazow/");
            }
            catch (IOException ex2)
            {
                ex2.printStackTrace();
            }
        }
        else 
        {
            try 
            {
                Runtime.getRuntime().exec("eog " + s + " /Pulpit/Java/przetwarzanie_obrazow/");
            }
            catch (IOException ex3)
            {
                ex3.printStackTrace();
            }
        }
    }
    
    void IsPGM(String s) 
    {
        if (s.equals("P2")) 
        {
            System.out.println("It is PGM file");
        }
        else 
        {
            System.out.println("It is not PGM file");
            System.exit(0);
        }
    }
    
    void ReadPicture() 
    {
        try 
        {
            Scanner scanner = new Scanner(new File(this.convertedImage));
            System.out.println("File found");
            this.IsPGM(scanner.next());
            scanner.nextLine();
            this.comment = scanner.nextLine();
            this.sizeX = scanner.nextInt();
            this.sizeY = scanner.nextInt();
            this.maxGreyLevel = scanner.nextInt();
            this.pictureMap = new int[this.sizeY][this.sizeX];
            for (int i = 0; i < this.sizeY; ++i) {
                for (int j = 0; j < this.sizeX; ++j) {
                    this.pictureMap[i][j] = scanner.nextInt();
                }
            }
            System.out.println("Picture read successfully");
            this.Display(this.convertedImage);
            scanner.close();
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("File not found");
            ex.printStackTrace();
        }
    }
    
    boolean IsSavedAsPGM(String s) 
    {
        if (s.substring(s.length() - 4, s.length()).equals(".pgm")) 
        {
            System.out.println("Saving as .pgm file");
            return true;
        }
        System.out.println("An error occured");
        return false;
    }
    
    void WriteToFile(String fileName) 
    {
        try 
        {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write("P2 \n");
            fileWriter.write(this.comment + "\n");
            fileWriter.write(this.sizeX + " " + this.sizeY +"\n");
            fileWriter.write(this.maxGreyLevel + "\n");
            for (int i = 0; i < this.sizeY; i++) {
                for (int j = 0; j < this.sizeX; j++) {
                    fileWriter.write(this.pictureMap[i][j]+" ");
                }
                fileWriter.write("\n");
            }
            fileWriter.close();
        }
        catch (IOException ex) 
        {
            System.out.println("An error occured.");
            ex.printStackTrace();
        }
    }
    
    void SavePicture() 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Save as: ");
        String next = scanner.next();
        if (!this.IsSavedAsPGM(next)) {
            System.exit(0);
        }
        File file = new File(next);
        try 
        {
            if (file.createNewFile()) 
            {
                System.out.println("Created new file " + file.getName());
                this.WriteToFile(next);
            }
            else 
            {
                System.out.println("File already exists.");
                System.exit(-1);
            }
        }
        catch (IOException ex) 
        {
            System.out.println("An error occured.");
            ex.printStackTrace();
        }
    }
    
    void Negative() 
    {
        for (int i = 0; i < this.sizeY; ++i) {
            for (int j = 0; j < this.sizeX; ++j) {
                this.pictureMap[i][j] = this.maxGreyLevel - this.pictureMap[i][j];
            }
        }
        this.Display("tmp.pgm");
    }
    
    void Thresholding() 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter thresholding level (1-100): ");
        int nextInt = scanner.nextInt();
        if (nextInt < 1 || nextInt > 100) {
            System.out.println("Try again");
            System.exit(0);
        }
        for (int i = 0; i < this.sizeY; ++i) {
            for (int j = 0; j < this.sizeX; ++j) {
                if (this.pictureMap[i][j] <= nextInt) {
                    this.pictureMap[i][j] = 0;
                }
                else {
                    this.pictureMap[i][j] = this.maxGreyLevel;
                }
            }
        }
        this.Display("tmp.pgm");
    }
    
    void Contouring() 
    {
        for (int i = 0; i < this.sizeY - 1; ++i){
            for (int j = 0; j < this.sizeX - 1; ++j) {
                this.pictureMap[i][j] = Math.abs(this.pictureMap[i + 1][j] - this.pictureMap[i][j]) + Math.abs(this.pictureMap[i][j + 1] - this.pictureMap[i][j]);
            }
        }
        this.Display("tmp.pgm");
    }
    
    void BlackAndWhiteLevel(int n, int n2) 
    {
        int n3 = n * this.maxGreyLevel / 100;
        int n4 = n2 * this.maxGreyLevel / 100;
        for (int i = 0; i < this.sizeY; ++i) {
            for (int j = 0; j < this.sizeX; ++j) {
                if (this.pictureMap[i][j] <= n3) {
                    this.pictureMap[i][j] = 0;
                }
                else if (this.pictureMap[i][j] >= n4) {
                    this.pictureMap[i][j] = this.maxGreyLevel;
                }
                else {
                    this.pictureMap[i][j] = (this.pictureMap[i][j] - n3) * (this.maxGreyLevel / (n4 - n3));
                }
            }
        }
        this.Display("tmp.pgm");
    }
    
    void HorizontalBlur() 
    {
        int[][] array = new int[this.sizeY][this.sizeX];
        for (int i = 1; i < this.sizeY - 1; ++i) {
            for (int j = 1; j < this.sizeX - 1; ++j) {
                array[i][j] = (this.pictureMap[i - 1][j] + this.pictureMap[i][j] + this.pictureMap[i + 1][j]) / 3;
            }
        }
        for (int k = 1; k < this.sizeY - 1; ++k) {
            for (int l = 1; l < this.sizeX - 1; ++l) {
                this.pictureMap[k][l] = array[k][l];
            }
        }
        this.Display("tmp.pgm");
    }
    
    void VerticalBlur()
    {
        int[][] array = new int[this.sizeY][this.sizeX];
        for (int i = 1; i < this.sizeY - 1; ++i) {
            for (int j = 1; j < this.sizeX - 1; ++j) {
                array[i][j] = (this.pictureMap[i][j - 1] + this.pictureMap[i][j] + this.pictureMap[i][j + 1]) / 3;
            }
        }
        for (int k = 1; k < this.sizeY - 1; ++k) {
            for (int l = 1; l < this.sizeX - 1; ++l) {
                this.pictureMap[k][l] = array[k][l];
            }
        }
        this.Display("tmp.pgm");
    }
    
    void GammaCorrection() 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Type gamma: ");
        float nextFloat = scanner.nextFloat();
        for (int i = 0; i < this.sizeY; ++i) {
            for (int j = 0; j < this.sizeX; ++j) {
                this.pictureMap[i][j] = (int)(Math.pow((float)this.pictureMap[i][j], 1.0f / nextFloat) / Math.pow((float)this.maxGreyLevel, (1.0f - nextFloat) / nextFloat));
            }
        }
        this.Display("tmp.pgm");
    }
    
    void DisplayMenu() 
    {
        System.out.println();
        System.out.println("r     Read image");
        System.out.println("s     Save image");
        System.out.println("n     Negative");
        System.out.println("t     Thresholding");
        System.out.println("c     Contouring");
        System.out.println("l     Black and white level");
        System.out.println("v     Vertical blur");
        System.out.println("h     Horizontal blur");
        System.out.println("g     Gamma correction");
        System.out.println("e     End");
        System.out.println("m     Menu");
        System.out.println();
    }
    
    public static void main(String[] array) 
    {
        ImageConvert imageConvert = new ImageConvert();
        boolean b = false;
        Character c = '\0';
        Scanner scanner = new Scanner(System.in);
        imageConvert.DisplayMenu();
        while (c != 'e') 
        {
            System.out.print("\nYour choice > ");
            c = scanner.next().charAt(0);
            switch ((char)c) 
            {
                case 'r': 
                {
                    System.out.print("Type file name: ");
                    imageConvert.convertedImage = scanner.next();
                    imageConvert.ReadPicture();
                    b = true;
                    continue;
                }
                case 's': 
                {
                    if (b) 
                    {
                        imageConvert.SavePicture();
                        continue;
                    }
                    System.out.println("First read your picture");
                    continue;
                }
                case 'n': 
                {
                    if (b) 
                    {
                        imageConvert.Negative();
                        System.out.println("Negative done");
                        continue;
                    }
                    System.out.println("First read your picture");
                    continue;
                }
                case 'c': 
                {
                    if (b) 
                    {
                        imageConvert.Contouring();
                        System.out.println("Contouring done");
                        continue;
                    }
                    System.out.println("First read your picture");
                    continue;
                }
                case 't': 
                {
                    if (b) 
                    {
                        imageConvert.Thresholding();
                        System.out.println("Thresholding done");
                        continue;
                    }
                    System.out.println("First read your picture");
                    continue;
                }
                case 'l': 
                {
                    if (b) 
                    {
                        Scanner scanner2 = new Scanner(System.in);
                        System.out.println("White and black must belong to (1-100)");
                        System.out.println("White must be bigger than black");
                        System.out.print("Type black level: ");
                        int nextInt = scanner2.nextInt();
                        System.out.print("Type white level: ");
                        int nextInt2 = scanner2.nextInt();
                        if (nextInt > 0 && nextInt <= 100 && nextInt2 > 0 && nextInt2 <= 100 && nextInt2 > nextInt) 
                        {
                            imageConvert.BlackAndWhiteLevel(nextInt, nextInt2);
                            System.out.println("\nLevels changing done");
                        }
                        else 
                        {
                            System.out.println("Try again");
                        }
                        continue;
                    }
                    System.out.println("First read your picture");
                    continue;
                }
                case 'v': 
                {
                    if (b) 
                    {
                        imageConvert.VerticalBlur();
                        System.out.println("Vertical blur done");
                        continue;
                    }
                    System.out.println("First read your picture");
                    continue;
                }
                case 'h': 
                {
                    if (b) 
                    {
                        imageConvert.HorizontalBlur();
                        System.out.println("Horizontal blur done");
                        continue;
                    }
                    System.out.println("First read your picture");
                    continue;
                }
                case 'g': 
                {
                    if (b) 
                    {
                        imageConvert.GammaCorrection();
                        System.out.println("Gamma Correction done");
                        continue;
                    }
                    System.out.println("First read your picture");
                    continue;
                }
                default: 
                {
                    System.out.println("Bad flag");
                    continue;
                }
                case 'm': 
                {
                    imageConvert.DisplayMenu();
                    continue;
                }
                case 'e': 
                {
                    System.out.println("Thanks for using!");
                    imageConvert.RemoveTmp();
                    continue;
                }
            }
        }
    }
}
