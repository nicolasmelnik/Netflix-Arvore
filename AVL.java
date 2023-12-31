// Nome dos Integrantes:
// Caio Alexandre V.B. de Andrade, TIA - 32229690.
// Diego Oliveira Aluizio, TIA - 32247591.
// Nicolas Fernandes Melnik, TIA - 32241720.

import java.util.ArrayList;
import java.util.List;

public class AVL extends BST {

    public AVL() {
        super();
    }

    public AVL(Node root) {
        super(root);
    }

    public Node rotateLeft() {
        return rotateLeft(root);
    }

    private Node rotateLeft(Node root) {
        Node right = root.getRight();
        right.setParent(root.getParent());
        root.setRight(right.getLeft());

        if (root.getRight() != null) {
            root.getRight().setParent(root);
        }

        right.setLeft(root);
        root.setParent(right);

        if (right.getParent() != null) {
            if (right.getParent().getRight() == root) {
                right.getParent().setRight(right);
            } else if (right.getParent().getLeft() == root) {
                right.getParent().setLeft(right);
            }
        }

        root.updateBalanceFactor();
        right.updateBalanceFactor();
        return right;
    }

    public Node rotateRight() {
        return rotateRight(root);
    }

    private Node rotateRight(Node root) {
        Node left = root.getLeft();
        left.setParent(root.getParent());
        root.setLeft(left.getRight());

        if (root.getLeft() != null) {
            root.getLeft().setParent(root);
        }

        left.setRight(root);
        root.setParent(left);

        if (left.getParent() != null) {
            if (left.getParent().getRight() == root) {
                left.getParent().setRight(left);
            } else if (left.getParent().getLeft() == root) {
                left.getParent().setLeft(left);
            }
        }

        root.updateBalanceFactor();
        left.updateBalanceFactor();
        return left;
    }

    public Node rotateLeftRight() {
        return rotateLeftRight(root);
    }

    private Node rotateLeftRight(Node root) {
        root.setLeft(rotateLeft(root.getLeft()));
        return rotateRight(root);
    }

    public Node rotateRightLeft() {
        return rotateRightLeft(root);
    }

    private Node rotateRightLeft(Node root) {
        root.setRight(rotateRight(root.getRight()));
        return rotateLeft(root);
    }

    public void balanceNode(Node node) {
        node.updateBalanceFactor();
        int balanceFactor = node.getBalanceFactor();

        if (balanceFactor > 1) {
            if (node.getRight().getBalanceFactor() < 0) {
                rotateRightLeft(node);
            } else {
                rotateLeft(node);
            }
        } else if (balanceFactor < -1) {
            if (node.getLeft().getBalanceFactor() > 0) {
                rotateLeftRight(node);
            } else {
                rotateRight(node);
            }
        }
        if (node.getParent() != null) {
            balanceNode(node.getParent());
        } else {
            this.root = node;
        }
    }

    public void insert(ProgramaNetflix programa) {
        Node node = new Node(programa);
        insert(this.root, node);
    }

    private void insert(Node auxiliar, Node node) {
        if (auxiliar == null) {
            this.root = node;
        } else {
            int compareResult = node.getData().getId().compareTo(auxiliar.getData().getId());
            if (compareResult < 0) {
                if (auxiliar.getLeft() == null) {
                    auxiliar.setLeft(node);
                    node.setParent(auxiliar);
                    balanceNode(auxiliar);
                } else {
                    insert(auxiliar.getLeft(), node);
                }
            } else if (compareResult > 0) {
                if (auxiliar.getRight() == null) {
                    auxiliar.setRight(node);
                    node.setParent(auxiliar);
                    balanceNode(auxiliar);
                } else {
                    insert(auxiliar.getRight(), node);
                }
            } else {
                System.out.println("Não é possível inserir pois a chave '" + node.getData().getId() + "' já existe!\n");
            }
        }
    }

    public void remove(String programaId) {
        remove(this.root, programaId);
    }

    private void remove(Node node, String programaId) {
        if (node == null) {
            System.out.println("Programa a ser removido não encontrado na AVL!\n");
            return;
        } else {
            int compareResult = programaId.compareTo(node.getData().getId());
            if (compareResult < 0) {
                remove(node.getLeft(), programaId);
            } else if (compareResult > 0) {
                remove(node.getRight(), programaId);
            } else if (compareResult == 0) {
                System.out.println("Programa removido da AVL com sucesso!\n");
                removerNoEncontrado(node);
            }
        }
    }

    private void removerNoEncontrado(Node node) {
        Node r;
        if (node.getLeft() == null || node.getRight() == null) {
            if (node.getParent() == null) {
                this.root = null;
                node = null;
                return;
            }
            r = node;
        } else {
            r = findPredecessor(node.getData());
            node.setData(r.getData());
        }
        Node p;
        if (r.getLeft() != null) {
            p = r.getLeft();
        } else {
            p = r.getRight();
        }
        if (p != null) {
            p.setParent(r.getParent());
        }
        if (r.getParent() == null) {
            this.root = p;
        } else {
            if (r == r.getParent().getLeft()) {
                r.getParent().setLeft(p);
            } else {
                r.getParent().setRight(p);
            }
            balanceNode(r.getParent());
        }
        r = null;
    }

    // Consultas:
    public void topCrimesAnos2000() {
        List<ProgramaNetflix> topTitles = new ArrayList<>();
        topCrimesAnos2000(root, topTitles);
        displayTopTitlesByScore(topTitles, "\nPrimeira Consulta - Top 10 programas de crime da década de 2000:");
    }

    private void topCrimesAnos2000(Node node, List<ProgramaNetflix> result) {
        if (node != null) {
            topCrimesAnos2000(node.getRight(), result);
            topCrimesAnos2000(node.getLeft(), result);
            ProgramaNetflix programa = node.getData();
            if ((programa.getReleaseYear() >= 2000 && programa.getReleaseYear() < 2010)
                    && programa.getGeneros().contains("crime")) {
                result.add(programa);
            }
        }
    }

    private void displayTopTitlesByScore(List<ProgramaNetflix> titles, String message) {
        System.out.println(message);
        int numTitlesToDisplay = Math.min(titles.size(), 10);

        for (int i = 0; i < numTitlesToDisplay; i++) {
            ProgramaNetflix maiorNota = titles.get(i);
            for (ProgramaNetflix programa : titles) {
                if (programa.getImdbScore() > maiorNota.getImdbScore()) {
                    maiorNota = programa;
                }
            }
            System.out.println(i + 1 + "° -> " + maiorNota.getTitulo() + ", " + maiorNota.getReleaseYear()
                    + " - Score: " + maiorNota.getImdbScore());
            titles.remove(maiorNota);
        }
        System.out.println();
    }

    public void topWarProgramsByVotes() {
        List<ProgramaNetflix> topTitles = new ArrayList<>();
        topWarProgramsByVotes(root, topTitles);
        displayTopTitlesByVotes(topTitles,
                "Segunda Consulta - Top 15 programas que tenham tema 'war' por votos no IMDb:");
    }

    private void topWarProgramsByVotes(Node node, List<ProgramaNetflix> result) {
        if (node != null) {
            ProgramaNetflix programa = node.getData();
            if (programa.getGeneros().contains("war")) {
                result.add(programa);
            }
            topWarProgramsByVotes(node.getRight(), result);
            topWarProgramsByVotes(node.getLeft(), result);
        }
    }

    private void displayTopTitlesByVotes(List<ProgramaNetflix> titles, String message) {
        System.out.println(message);
        int numTitlesToDisplay = Math.min(titles.size(), 15);
        for (int i = 0; i < numTitlesToDisplay; i++) {
            ProgramaNetflix mostVoted = titles.get(0);
            for (ProgramaNetflix programa : titles) {
                if (programa.getImdbVotes() > mostVoted.getImdbVotes()) {
                    mostVoted = programa;
                }
            }
            System.out.println(i + 1 + "° -> " + mostVoted.getTitulo() + " - " + mostVoted.getGeneros()
                    + " - IMDb Votes: " + mostVoted.getImdbVotes());
            titles.remove(mostVoted);
        }
        System.out.println();
    }

    public void topMovies2022ByScore() {
        List<ProgramaNetflix> topMovies = new ArrayList<>();
        topMovies2022ByScore(root, topMovies);
        displayTopMoviesByScore(topMovies, "Terceira Consulta - Top 10 filmes de 2022 por pontuação no IMDb:");
    }

    private void topMovies2022ByScore(Node node, List<ProgramaNetflix> result) {
        if (node != null) {
            topMovies2022ByScore(node.getRight(), result);

            ProgramaNetflix programa = node.getData();
            if (programa.getShowType().equalsIgnoreCase("MOVIE") && programa.getReleaseYear() == 2022) {
                result.add(programa);
            }
            topMovies2022ByScore(node.getLeft(), result);
        }
    }

    private void displayTopMoviesByScore(List<ProgramaNetflix> movies, String message) {
        System.out.println(message);
        int numMoviesToDisplay = Math.min(movies.size(), 10);
        for (int i = 0; i < numMoviesToDisplay; i++) {
            ProgramaNetflix topMovie = movies.get(0);
            for (ProgramaNetflix movie : movies) {
                if (movie.getImdbScore() > topMovie.getImdbScore()) {
                    topMovie = movie;
                }
            }
            System.out.println(i + 1 + "° -> " + topMovie.getTitulo() + " - IMDb Score: " + topMovie.getImdbScore());
            movies.remove(topMovie);
        }
        System.out.println();
    }

    public void topTVShowsByScore() {
        List<ProgramaNetflix> topTVShows = new ArrayList<>();
        topTVShowsByScore(root, topTVShows);
        displayTopTVShowsByScore(topTVShows, "Quarta Consulta - Top 10 programas de TV por pontuação no IMDb:");
    }

    private void topTVShowsByScore(Node node, List<ProgramaNetflix> result) {
        if (node != null) {
            topTVShowsByScore(node.getRight(), result);
            ProgramaNetflix programa = node.getData();
            if (programa.getShowType().equalsIgnoreCase("SHOW")) {
                result.add(programa);
            }
            topTVShowsByScore(node.getLeft(), result);
        }
    }

    private void displayTopTVShowsByScore(List<ProgramaNetflix> tvShows, String message) {
        System.out.println(message);
        int numTVShowsToDisplay = Math.min(tvShows.size(), 10);
        for (int i = 0; i < numTVShowsToDisplay; i++) {
            ProgramaNetflix topTVShow = tvShows.get(0);
            for (ProgramaNetflix tvShow : tvShows) {
                if (tvShow.getImdbScore() > topTVShow.getImdbScore()) {
                    topTVShow = tvShow;
                }
            }
            System.out.println(i + 1 + "° -> " + topTVShow.getTitulo() + " - IMDb Score: " + topTVShow.getImdbScore());
            tvShows.remove(topTVShow);
        }
        System.out.println();
    }

    public void topOldestPrograms() {
        List<ProgramaNetflix> oldestPrograms = new ArrayList<>();
        topOldestPrograms(root, oldestPrograms);
        displayTopOldestPrograms(oldestPrograms, "Quinta Consulta - Top 10 programas mais antigos disponíveis:");
    }

    private void topOldestPrograms(Node node, List<ProgramaNetflix> result) {
        if (node != null) {
            topOldestPrograms(node.getRight(), result);
            ProgramaNetflix programa = node.getData();
            result.add(programa);
            topOldestPrograms(node.getLeft(), result);
        }
    }

    private void displayTopOldestPrograms(List<ProgramaNetflix> programs, String message) {
        System.out.println(message);
        int numProgramsToDisplay = Math.min(programs.size(), 10);
        for (int i = 0; i < numProgramsToDisplay; i++) {
            ProgramaNetflix oldestProgram = programs.get(0);
            for (ProgramaNetflix program : programs) {
                if (program.getReleaseYear() < oldestProgram.getReleaseYear()) {
                    oldestProgram = program;
                }
            }
            System.out.println(i + 1 + "° -> " + oldestProgram.getTitulo() + " - Ano de Lançamento: "
                    + oldestProgram.getReleaseYear());
            programs.remove(oldestProgram);
        }
        System.out.println();
    }

    public List<ProgramaNetflix> inOrderTraversalList() {
        List<ProgramaNetflix> programas = new ArrayList<>();
        inOrderTraversalList(root, programas);
        return programas;
    }

    private void inOrderTraversalList(Node node, List<ProgramaNetflix> programas) {
        if (node != null) {
            inOrderTraversalList(node.getLeft(), programas);
            programas.add(node.getData());
            inOrderTraversalList(node.getRight(), programas);
        }
    }
}