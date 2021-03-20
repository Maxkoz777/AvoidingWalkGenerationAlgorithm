import io.jenetics.Genotype;
import io.jenetics.IntegerChromosome;
import io.jenetics.IntegerGene;
import io.jenetics.util.Factory;

import java.util.ArrayList;

public class ModelFactory {

    public static Factory<Genotype<IntegerGene>> of (int n) {

        ArrayList<IntegerChromosome> chromosomes = new ArrayList<>();

        for (int i = 0; i < 1; i++) {

            ArrayList<IntegerGene> genes = new ArrayList<>();

            for (int j = 0; j < MainApp.RANK * MainApp.RANK; j++) {

                genes.add(IntegerGene.of(1, 4));

            }

            chromosomes.add(IntegerChromosome.of(genes));

        }

        return Genotype.of(chromosomes);

    }

}
