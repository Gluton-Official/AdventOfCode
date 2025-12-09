use std::cmp::Ordering;
use std::collections::{HashMap, HashSet};
use std::fmt::{Debug, Formatter};
use std::hash::{Hash, Hasher};
use std::str::Lines;
use indoc::indoc;
use crate::util::aocpuzzle::{AOCPuzzle, Test};

#[derive(Ord, Eq, PartialEq, Hash, Copy, Clone)]
struct JunctionBox(isize, isize, isize);
impl JunctionBox {
    fn vectorize(&self) -> f64 {
        ((self.0 + self.1 + self.2) as f64).cbrt()
    }

    fn distance(&self, other: JunctionBox) -> f64 {
        (((self.0 - other.0).pow(2) + (self.1 - other.1).pow(2) + (self.2 - other.2).pow(2)) as f64).sqrt()
    }
}
impl PartialOrd for JunctionBox {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        self.vectorize().partial_cmp(&other.vectorize())
    }
}
impl Debug for JunctionBox {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        f.debug_tuple("")
            .field(&self.0)
            .field(&self.1)
            .field(&self.2)
            .finish()
    }
}

struct Connection {
    junction_boxes: [JunctionBox; 2],
    length: f64,
}
impl Debug for Connection {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        f.debug_set().entry(&self.length).entry(&self.junction_boxes).finish()
    }
}
impl Hash for Connection {
    fn hash<H: Hasher>(&self, state: &mut H) {
        let mut sorted = self.junction_boxes.clone();
        sorted.sort();
        sorted.hash(state);
    }
}
impl PartialEq<Self> for Connection {
    fn eq(&self, other: &Self) -> bool {
        self.junction_boxes[0] == other.junction_boxes[0] && self.junction_boxes[1] == other.junction_boxes[1]
            || self.junction_boxes[0] == other.junction_boxes[1] && self.junction_boxes[1] == other.junction_boxes[0]
    }
}
impl Eq for Connection {}

struct Day08 {
    connection_limit: Option<usize>,
}

impl Day08 {
    fn new() -> Self {
        Day08 {
            connection_limit: None
        }
    }
    fn with_connection_limit(connection_limit: usize) -> Self {
        Day08 {
            connection_limit: Some(connection_limit)
        }
    }
    fn set_connection_limit(&mut self, connection_limit: usize) {
        self.connection_limit = Some(connection_limit);
    }

    fn parse(input: Lines<'_>) -> Vec<JunctionBox> {
        input.map(|line| {
            let [x, y, z] = line.split(',').map(|value| value.parse().unwrap()).collect::<Vec<_>>().try_into().unwrap();
            JunctionBox(x, y, z)
        }).collect()
    }

    fn build_connections(junction_boxes: &Vec<JunctionBox>) -> HashSet<Connection> {
        let mut connections: HashSet<Connection> = HashSet::new();
        for &junction_box in junction_boxes {
            for &other_junction_box in junction_boxes {
                if junction_box != other_junction_box {
                    let connection = Connection {
                        junction_boxes: [junction_box, other_junction_box],
                        length: junction_box.distance(other_junction_box),
                    };
                    connections.insert(connection);
                }
            }
        }
        connections
    }

    fn build_circuits(connections: Vec<Connection>) -> Vec<HashSet<JunctionBox>> {
        let mut junction_box_circuit: HashMap<JunctionBox, usize> = HashMap::new();
        let mut circuits: HashMap<usize, HashSet<JunctionBox>> = HashMap::new();
        let mut next_circuit_id: usize = 0;
        let mut new_circuit_id = || -> usize {
            let new_circuit_id = next_circuit_id;
            next_circuit_id += 1;
            new_circuit_id
        };
        for connection in connections {
            let [first_junction_box, second_junction_box] = connection.junction_boxes;
            match (junction_box_circuit.get(&first_junction_box), junction_box_circuit.get(&second_junction_box)) {
                (Some(&first_circuit_id), Some(&second_circuit_id)) => {
                    if first_circuit_id != second_circuit_id {
                        let other_circuit = circuits.remove(&second_circuit_id).unwrap();
                        let circuit = circuits.get_mut(&first_circuit_id).unwrap();
                        for junction_box in other_circuit {
                            let junction_box_circuit_id = junction_box_circuit.get_mut(&junction_box).unwrap();
                            *junction_box_circuit_id = first_circuit_id;
                            circuit.insert(junction_box);
                        }
                    }
                },
                (Some(&circuit_id), None) => {
                    junction_box_circuit.insert(second_junction_box, circuit_id);
                    circuits.get_mut(&circuit_id).unwrap().insert(second_junction_box);
                }
                (None, Some(&circuit_id)) => {
                    junction_box_circuit.insert(first_junction_box, circuit_id);
                    circuits.get_mut(&circuit_id).unwrap().insert(first_junction_box);
                }
                (None, None) => {
                    let circuit_id = new_circuit_id();
                    junction_box_circuit.insert(first_junction_box, circuit_id);
                    junction_box_circuit.insert(second_junction_box, circuit_id);
                    circuits.insert(circuit_id, vec![first_junction_box, second_junction_box].into_iter().collect());
                }
            }
        }
        circuits.iter().map(|(_, junction_boxes)| junction_boxes.clone()).collect()
    }

    fn build_circuits_until_single_circuit(junction_boxes: Vec<JunctionBox>, connections: Vec<Connection>) -> Option<Connection> {
        let mut disconnected_junction_boxes: HashSet<JunctionBox> = junction_boxes.into_iter().collect();
        let mut junction_box_circuit: HashMap<JunctionBox, usize> = HashMap::new();
        let mut circuits: HashMap<usize, HashSet<JunctionBox>> = HashMap::new();
        let mut next_circuit_id: usize = 0;
        let mut new_circuit_id = || -> usize {
            let new_circuit_id = next_circuit_id;
            next_circuit_id += 1;
            new_circuit_id
        };
        for connection in connections {
            let [first_junction_box, second_junction_box] = connection.junction_boxes;
            match (junction_box_circuit.get(&first_junction_box), junction_box_circuit.get(&second_junction_box)) {
                (Some(&first_circuit_id), Some(&second_circuit_id)) => {
                    if first_circuit_id != second_circuit_id {
                        let other_circuit = circuits.remove(&second_circuit_id).unwrap();
                        let circuit = circuits.get_mut(&first_circuit_id).unwrap();
                        for junction_box in other_circuit {
                            let junction_box_circuit_id = junction_box_circuit.get_mut(&junction_box).unwrap();
                            *junction_box_circuit_id = first_circuit_id;
                            circuit.insert(junction_box);
                        }
                    }
                },
                (Some(&circuit_id), None) => {
                    junction_box_circuit.insert(second_junction_box, circuit_id);
                    circuits.get_mut(&circuit_id).unwrap().insert(second_junction_box);
                    disconnected_junction_boxes.remove(&second_junction_box);
                }
                (None, Some(&circuit_id)) => {
                    junction_box_circuit.insert(first_junction_box, circuit_id);
                    circuits.get_mut(&circuit_id).unwrap().insert(first_junction_box);
                    disconnected_junction_boxes.remove(&first_junction_box);
                }
                (None, None) => {
                    let circuit_id = new_circuit_id();
                    junction_box_circuit.insert(first_junction_box, circuit_id);
                    junction_box_circuit.insert(second_junction_box, circuit_id);
                    circuits.insert(circuit_id, vec![first_junction_box, second_junction_box].into_iter().collect());
                    disconnected_junction_boxes.remove(&first_junction_box);
                    disconnected_junction_boxes.remove(&second_junction_box);
                }
            }
            // println!("{disconnected_junction_boxes:?}\n\t{circuits:?}");
            if disconnected_junction_boxes.is_empty() && circuits.len() == 1 {
                return Some(connection);
            }
        }
        None
    }
}

impl AOCPuzzle<08, 2025> for Day08 {
    fn part1_tests(&self) -> Vec<Test> {
        vec![
            Test {
                expected: 40,
                input: indoc! {"
                    162,817,812
                    57,618,57
                    906,360,560
                    592,479,940
                    352,342,300
                    466,668,158
                    542,29,236
                    431,825,988
                    739,650,466
                    52,470,668
                    216,146,977
                    819,987,18
                    117,168,530
                    805,96,715
                    346,949,466
                    970,615,88
                    941,993,340
                    862,61,35
                    984,92,344
                    425,690,689
                "}.trim().into()
            }
        ]
    }
    fn part1(&self, input: Lines<'_>) -> usize {
        let junction_boxes = Self::parse(input);
        let connections = Self::build_connections(&junction_boxes);
        let mut connections = connections.into_iter().collect::<Vec<Connection>>();
        connections.sort_by(|connection1, connection2| connection1.length.total_cmp(&connection2.length));
        let connections = connections.into_iter().take(self.connection_limit.expect("Part 1 requires a connection limit")).collect::<Vec<_>>();
        let mut circuits = Self::build_circuits(connections);
        circuits.sort_by_key(|circuit| circuit.len());
        circuits.reverse();
        circuits.iter().take(3).fold(1, |acc, circuit| acc * circuit.len())
    }

    fn part2_tests(&self) -> Vec<Test> {
        vec![
            Test {
                expected: 25272,
                input: indoc! {"
                    162,817,812
                    57,618,57
                    906,360,560
                    592,479,940
                    352,342,300
                    466,668,158
                    542,29,236
                    431,825,988
                    739,650,466
                    52,470,668
                    216,146,977
                    819,987,18
                    117,168,530
                    805,96,715
                    346,949,466
                    970,615,88
                    941,993,340
                    862,61,35
                    984,92,344
                    425,690,689
                "}.trim().into()
            }
        ]
    }
    fn part2(&self, input: Lines<'_>) -> usize {
        let junction_boxes = Self::parse(input);
        let connections = Self::build_connections(&junction_boxes);
        let mut connections = connections.into_iter().collect::<Vec<Connection>>();
        connections.sort_by(|connection1, connection2| connection1.length.total_cmp(&connection2.length));
        let final_connection = Self::build_circuits_until_single_circuit(junction_boxes, connections).unwrap();
        let [first_x, second_x] = final_connection.junction_boxes.map(|junction_box| junction_box.0);
        (first_x * second_x) as usize
    }
}

#[cfg(test)]
mod test {
    use crate::util::aocpuzzle::AOCPuzzleRunner;
    use super::*;

    #[test]
    fn test_part1() {
        let day = Day08::with_connection_limit(10);
        let mut runner = AOCPuzzleRunner::new(day);

        let success = runner.test_part1();
        assert!(success);
        if success {
            runner.puzzle.set_connection_limit(1000);
            // runner.run_part1();
            assert_eq!(123930, runner.run_part1());
        }
    }

    #[test]
    fn test_part2() {
        let day = Day08::new();
        let runner = AOCPuzzleRunner::new(day);

        let success = runner.test_part2();
        assert!(success);
        if success {
            // runner.run_part2();
            assert_eq!(27338688, runner.run_part2());
        }
    }
}