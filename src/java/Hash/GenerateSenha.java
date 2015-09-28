package hash;
public class GenerateSenha
{
    
    public static String generateNewSenha()
    {
        String senha = "";
        final char[] alfa = {'A','B','C','D','E','F','G','H','I','J','K','L','M'
            ,'N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        
        MersenneTwisterFast r = new MersenneTwisterFast();
                
        for(int i = 0; i < 16; i++)
        {
            long random = r.nextLong(2);
            if(random == 0)
            {
                senha += r.nextLong(10);
            }
            else if(random == 1)
            {
                senha += alfa[(int)r.nextLong(26)];
            }
        }
        return senha;
    }
}