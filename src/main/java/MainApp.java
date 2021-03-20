import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.engine.Limits;
import io.jenetics.util.Factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.Scanner;

public class MainApp {

    static final int RANK;

    static final Scanner in = new Scanner(System.in);

    static {
        System.out.println("Enter matrix rank:");
        RANK = in.nextInt();
    }

    static class Coordinate {
        int x;
        int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Coordinate that = (Coordinate) o;

            if (x != that.x) return false;
            return y == that.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

        @Override
        public String toString() {
            return "Coordinate{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    private static int eval(Genotype<IntegerGene> genotype) {

        Chromosome<IntegerGene> chromosome = genotype.chromosome().as(IntegerChromosome.class);

        ListIterator<IntegerGene> iterator = chromosome.listIterator();

        ArrayList<Integer> list = new ArrayList<>();

        while (iterator.hasNext()) {
            list.add(iterator.next().intValue());
        }

        return findPath(list);

    }

    private static int findPath(ArrayList<Integer> list) {

        Coordinate start = new Coordinate(0, 0);

        ArrayList<Coordinate> coordinates = new ArrayList<>();

        coordinates.add(start);

        Coordinate last;

        for (int k : list) {

            last = coordinates.get(coordinates.size() - 1);

            Coordinate coordinate;

            switch (k) {
                case 1 -> coordinate = new Coordinate(last.x - 1, last.y);
                case 2 -> coordinate = new Coordinate(last.x, last.y + 1);
                case 3 -> coordinate = new Coordinate(last.x + 1, last.y);
                case 4 -> coordinate = new Coordinate(last.x, last.y - 1);
                default -> throw new IllegalStateException("Unexpected value: " + k);
            }

            if (coordinates.contains(coordinate)){
                break;
            }

            if (Math.max(coordinate.x, coordinate.y) > (RANK - 1) || Math.min(coordinate.x, coordinate.y) < 0) {
                break;
            }

            coordinates.add(coordinate);

        }

        return coordinates.size();
    }

    public static void main(String[] args) {

        Factory<Genotype<IntegerGene>> factory = ModelFactory.of(5);

        Engine<IntegerGene, Integer> engine = Engine.builder(MainApp::eval, factory)
                .populationSize(500)
                .optimize(Optimize.MAXIMUM)
                .build();

        final EvolutionStatistics<Integer, ?> statistics = EvolutionStatistics.ofNumber();

        final Phenotype<IntegerGene, Integer> result = engine.stream()
                .limit(Limits.bySteadyFitness(10000))
                .peek(statistics)
                .collect(EvolutionResult.toBestPhenotype());

        System.out.println(statistics);

        System.out.println("Total path length: " + result.fitness() + "\n");

        System.out.println("Path:\n");

        vector2Coordinates((IntegerChromosome) result.genotype().chromosome());

    }

    static void vector2Coordinates(IntegerChromosome ch) {

        ArrayList<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(0, 0));
        for (int i = 0; i < RANK * RANK; i++) {

            Coordinate last = coordinates.get(coordinates.size() - 1);

            IntegerGene gene = (IntegerGene) ch.get(i);

            int k = gene.allele();

            switch (k) {
                case 1 -> coordinates.add( new Coordinate(last.x - 1, last.y));
                case 2 -> coordinates.add( new Coordinate(last.x, last.y + 1));
                case 3 -> coordinates.add( new Coordinate(last.x + 1, last.y));
                case 4 -> coordinates.add( new Coordinate(last.x, last.y - 1));
                default -> throw new IllegalStateException("Unexpected value: " + k);
            }

        }

        for (Coordinate coordinate : coordinates) {
            System.out.println(coordinate);
        }

    }

}
