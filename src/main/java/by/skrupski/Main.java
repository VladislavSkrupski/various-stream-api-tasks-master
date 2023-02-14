package by.skrupski;

import by.skrupski.model.Animal;
import by.skrupski.model.Car;
import by.skrupski.model.Flower;
import by.skrupski.model.House;
import by.skrupski.model.Person;
import by.skrupski.util.Util;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {
        task1();
        task2();
        task3();
        task4();
        task5();
        task6();
        task7();
        task8();
        task9();
        task10();
        task11();
        task12();
        task13();
        task14();
        task15();
    }

    private static void task1() throws IOException {
        List<Animal> animals = Util.getAnimals();
        animals.stream()
                .filter(animal -> animal.getAge() >= 10 && animal.getAge() < 20)
                .sorted(Comparator.comparingInt(Animal::getAge))
                .skip(7 * 2)
                .limit(7)
                .forEach(System.out::println);
    }

    private static void task2() throws IOException {
        List<Animal> animals = Util.getAnimals();
        animals.stream()
                .filter(animal -> "Japanese".equals(animal.getOrigin()))
                .peek(animal -> animal.setBread(animal.getBread().toUpperCase()))
                .filter(animal -> "Female".equals(animal.getGender()))
                .map(Animal::getBread)
                .forEach(System.out::println);
    }

    private static void task3() throws IOException {
        List<Animal> animals = Util.getAnimals();
        animals.stream()
                .filter(animal -> animal.getAge() > 30)
                .map(Animal::getOrigin)
                .filter(s -> s.startsWith("A"))
                .distinct()
                .forEach(System.out::println);
    }

    private static void task4() throws IOException {
        List<Animal> animals = Util.getAnimals();
        System.out.println(
                animals.stream()
                        .filter(animal -> "Female".equals(animal.getGender()))
                        .count()
        );
    }

    private static void task5() throws IOException {
        List<Animal> animals = Util.getAnimals();
        System.out.println(
                animals.stream()
                        .filter(animal -> animal.getAge() >= 20 && animal.getAge() <= 30)
                        .anyMatch(animal -> "Hungarian".equals(animal.getOrigin()))
        );
    }

    private static void task6() throws IOException {
        List<Animal> animals = Util.getAnimals();
        System.out.println(
                animals.stream()
                        .allMatch(animal -> "Male".equals(animal.getGender()) || "Female".equals(animal.getGender()))
        );
    }

    private static void task7() throws IOException {
        List<Animal> animals = Util.getAnimals();
        System.out.println(
                animals.stream()
                        .noneMatch(animal -> animal.getOrigin().contains("Oceania"))
        );
    }

    private static void task8() throws IOException {
        List<Animal> animals = Util.getAnimals();
        animals.stream()
                .sorted(Comparator.comparing(Animal::getBread))
                .limit(100)
                .map(Animal::getAge)
                .max(Integer::compare)
                .ifPresent(System.out::println);
    }

    private static void task9() throws IOException {
        List<Animal> animals = Util.getAnimals();
        animals.stream()
                .map(Animal::getBread)
                .map(String::toCharArray)
                .map(chars -> chars.length)
                .min(Integer::compare)
                .ifPresent(System.out::println);
    }

    private static void task10() throws IOException {
        List<Animal> animals = Util.getAnimals();
        System.out.println(
                animals.stream()
                        .mapToInt(Animal::getAge)
                        .sum()
        );
    }

    private static void task11() throws IOException {
        List<Animal> animals = Util.getAnimals();
        animals.stream()
                .filter(animal -> "Indonesian".equals(animal.getOrigin()))
                .mapToInt(Animal::getAge)
                .average()
                .ifPresent(System.out::println);
    }

    private static void task12() throws IOException {
        List<Person> people = Util.getPersons();
        people.stream()
                .filter(person -> "Male".equals(person.getGender()))
                .filter(person -> Period.between(person.getDateOfBirth(), LocalDate.now()).getYears() >= 18)
                .filter(person -> Period.between(person.getDateOfBirth(), LocalDate.now()).getYears() < 27)
                .sorted(Comparator.comparing(Person::getRecruitmentGroup))
                .limit(200)
                .forEach(System.out::println);
    }

    private static void task13() throws IOException {
        List<House> houses = Util.getHouses();
        Predicate<Person> isSecondQueuePerson = (person) -> {
            int yearsOld = Period.between(person.getDateOfBirth(), LocalDate.now()).getYears();
            return yearsOld < 18
                    || (yearsOld >= 63 && "Male".equals(person.getGender()))
                    || (yearsOld >= 58 && "Female".equals(person.getGender()));
        };

        Map<Boolean, List<Person>> recentPeople = houses.stream()
                .filter(house -> !"Hospital".equals(house.getBuildingType()))
                .map(House::getPersonList)
                .flatMap(Collection::stream)
                .collect(Collectors.partitioningBy(isSecondQueuePerson));

        Stream<Person> firstQueuePeople = houses.stream()
                .filter(house -> "Hospital".equals(house.getBuildingType()))
                .map(House::getPersonList)
                .flatMap(Collection::stream);
        Stream<Person> secondQueuePeople = recentPeople.get(true).stream();
        Stream<Person> thirdQueuePeople = recentPeople.get(false).stream();
        Stream.concat(Stream.concat(firstQueuePeople, secondQueuePeople), thirdQueuePeople)
                .limit(500)
                .forEach(System.out::println);
    }

    /*
     * Очень хотелось бы посмотреть на хорошее и компактное решение 14 задачи.
     *
     * чую, что наворотил лишнего...
     */
    private static void task14() throws IOException {
        List<Car> cars = Util.getCars();
        List<List<Car>> echelons = new ArrayList<>();

        Predicate<Car> isGoingToTurkmenistan =
                (car) -> "Jaguar".equals(car.getCarMake()) || "White".equals(car.getColor());
        Predicate<Car> isGoingToUzbekistan =
                (car) -> car.getMass() < 1500 || List.of("BMW", "Lexus", "Chrysler", "Toyota").contains(car.getCarMake());
        Predicate<Car> isGoingToKazakhstan =
                (car) -> ("Black".equals(car.getColor()) && car.getMass() >= 4000) || List.of("GMC", "Dodge").contains(car.getCarMake());
        Predicate<Car> isGoingToKyrgyzstan =
                (car) -> car.getReleaseYear() < 1982 || List.of("Civic", "Cherokee").contains(car.getCarModel());
        Predicate<Car> isGoingToRussia =
                (car) -> !List.of("Yellow", "Red", "Green", "Blue").contains(car.getColor()) || car.getPrice() > 40000;
        Predicate<Car> isGoingToMongolia =
                (car) -> car.getVin().contains("59");

        cars.stream()
                .collect(Collectors.partitioningBy(isGoingToTurkmenistan))
                .forEach((key1, value1) -> {
                    if (key1) echelons.add(value1);
                    else value1.stream()
                            .collect(Collectors.partitioningBy(isGoingToUzbekistan))
                            .forEach((key2, value2) -> {
                                if (key2) echelons.add(value2);
                                else value2.stream()
                                        .collect(Collectors.partitioningBy(isGoingToKazakhstan))
                                        .forEach((key3, value3) -> {
                                            if (key3) echelons.add(value3);
                                            else value3.stream()
                                                    .collect(Collectors.partitioningBy(isGoingToKyrgyzstan))
                                                    .forEach((key4, value4) -> {
                                                        if (key4) echelons.add(value4);
                                                        else value4.stream()
                                                                .collect(Collectors.partitioningBy(isGoingToRussia))
                                                                .forEach((key5, value5) -> {
                                                                    if (key5) echelons.add(value5);
                                                                    else value5.stream()
                                                                            .collect(Collectors.partitioningBy(isGoingToMongolia))
                                                                            .forEach((key6, value6) -> {
                                                                                if (key6) echelons.add(value6);
                                                                            });
                                                                });
                                                    });
                                        });
                            });
                });

        Collections.reverse(echelons);
        echelons.stream()
                .map(carsInEchelons -> carsInEchelons.stream()
                        .map(Car::getMass)
                        .reduce(0, Integer::sum)
                        .doubleValue() * 7.14)
                .forEach(aDouble -> System.out.printf("%.2f\n", aDouble));

        System.out.printf("%.2f\n", echelons.stream()
                .map(carsInEchelons -> carsInEchelons.stream()
                        .map(car -> (car.getPrice() - car.getMass() * 7.14))
                        .reduce(0.0, Double::sum))
                .reduce(0.0, Double::sum)
        );
    }

    private static void task15() throws IOException {
        List<Flower> flowers = Util.getFlowers();
        //        Продолжить...
    }
}