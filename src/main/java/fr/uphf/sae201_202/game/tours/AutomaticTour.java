package fr.uphf.sae201_202.game.tours;

import fr.uphf.sae201_202.SAE;
import fr.uphf.sae201_202.game.Bot;
import fr.uphf.sae201_202.game.Utils;
import fr.uphf.sae201_202.maps.Cell;
import fr.uphf.sae201_202.maps.elements.Mine;
import fr.uphf.sae201_202.maps.elements.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AutomaticTour {

    private final Tour baseSystem;

    public AutomaticTour(Tour tour) throws InterruptedException {
        this.baseSystem = tour;
        this.baseSystem.setNbrTour(this.baseSystem.getNbrTour() + 1);
        this.baseSystem.setBotInTour(new ArrayList<>(SAE.get().getBots()));

        System.out.println("MAKING A NEW AUTOMATIC TOUR");
        makeTour();
    }

    private void makeTour() throws InterruptedException {
        for(Bot bot : baseSystem.getBotInTour()) {
            System.out.println("-*-*-*-*-*-*-*-*-*-*-");
            System.out.println("Action searching for Bot #" + bot.getId());
            applyAlgo(bot);
            for(Cell cells : Utils.getAdjacentCells(bot)) {
                if(SAE.get().getBotKnowsCells().contains(cells))
                    continue;

                SAE.get().getBotKnowsCells().add(cells);
                cells.getStyleClass().remove("unknowCase");
            }
            System.out.println("-*-*-*-*-*-*-*-*-*-*-");
        }
        baseSystem.getBotInTour().clear();
    }

    private void applyAlgo(Bot bot) {
        String action = searchAction(bot);
        if(action.equals("nothing")) {
            System.out.println("[ERROR] None action finded.");
            return;
        }
        Cell actualCell = SAE.get().getMap().getGrid().getCell(bot.getPosY(), bot.getPosX());

        if(action.startsWith("search")) {
            Cell selected = mostShortPath(bot, action);
            List<Cell> nextCell;
            nextCell = Dijstra.pathFinder(actualCell, selected);

            if(nextCell.isEmpty() || nextCell.size() == 1) {
                System.out.println("[ERROR] None path finded.");
                return;
            }
            nextCell.removeFirst();

            System.out.println("Finded a path : ");
            nextCell.forEach(c -> System.out.println("  - " + c.toString()));
            System.out.println("Next cell -> " + nextCell.getFirst().toString());
            Utils.moveToCell(bot, actualCell, nextCell.getFirst());
        } else {
            Utils.elementRunning(bot, actualCell);
        }
    }

    /*
    La fonction searchAction() sert à savoir qu'est-ce que le robot
    veut faire comme action, soit rien, soit trouver un entrepot,
    soit trouver une mine
     */
    public String searchAction(Bot bot) {
        boolean searchMine = false;
        boolean searchEntrepot = false;
        Cell actualCell = SAE.get().getMap().getGrid().getCell(bot.getPosY(), bot.getPosX());
        if (bot.getHarvest() != 0) {
            if(actualCell.getElement() != null && actualCell.getElement() instanceof Storage && ((Storage) actualCell.getElement()).getOres() == bot.getOres()) {
                System.out.println("Action -> drop ores");
                return "storage";
            }

            for (Cell cells : SAE.get().getBotKnowsCells()) {
                if (cells.getElement() != null &&
                        cells.getElement() instanceof Storage &&
                        ((Storage) cells.getElement()).getOres() == bot.getOres()) {
                    searchEntrepot = true;
                    break;
                }
            }
        }else {
            if(actualCell.getElement() != null && actualCell.getElement() instanceof Mine && ((Mine) actualCell.getElement()).getOreLess() > 0 && ((Mine) actualCell.getElement()).getOres() == bot.getOres()) {
                System.out.println("Action -> mining ores");
                return "mine";
            }

            for(Cell cells : SAE.get().getBotKnowsCells()) {
                if(cells.getElement() != null &&
                        cells.getElement() instanceof Mine &&
                        ((Mine) cells.getElement()).getOres() == bot.getOres()) {
                    searchMine = true;
                    break;
                }
            }
        }

        if (searchEntrepot) {
            System.out.println("Action -> going to storage");
            return "search storage";
        } else if (searchMine) {
            System.out.println("Action -> going to mine");
            return "search mine";
        } else {
            /*
             Si les deux sont en 'false', cela veut dire que dans les
             cellules que connaissent les robots, aucune cellule n'est une mine
             au bon minerai ou qu'aucun entrepot n'est trouvé avec le bon minerai.
             Si c'est le cas, le robot va simplement se déplacer de façon aléatoire
             pour essayer de trouver une mine ou un entrepot et aider ses coéquipiers robots.
             */
            System.out.println("Action -> searching random cell");
            Random random = new Random();

            /*
            xory correspond à x ou y qui est une partie de l'action aléatoire
            m1orp1 correspond à moins 1 ou plus 1 qui est l'autre partie de l'action aléatoire
             */
            boolean xory = random.nextBoolean();
            boolean m1orp1 = random.nextBoolean();
            Utils.actionMoving(bot, (xory ? "X" : "Y") + "/" + (m1orp1 ? "-" : "+"));
            return "nothing";
        }
    }

    /*
    La fonction mostShortPath() ou algorithme du plus court chemin
    est une fonction permettant de trouver l'élement précis que l'on souhaite
    en prenant compte de règles différentes dont la règle principale est que ça
    le le chemin le plus court possible.
     */
    public Cell mostShortPath(Bot bot, String element) {
        Cell selected = null;
        int minDistance = -1;

        for(Cell cells : Utils.getAdjacentCells(bot)) {
            if(element.contains("storage") && cells.getElement() != null && cells.getElement() instanceof Storage && ((Storage) cells.getElement()).getOres().equals(bot.getOres()))
                return cells;
            if (element.contains("mine") && cells.getElement() != null && cells.getElement() instanceof Mine && ((Mine) cells.getElement()).getOreLess() > 0 && ((Mine) cells.getElement()).getOres().equals(bot.getOres())) {
                return cells;
            }
        }

        List<Cell> elementList = new ArrayList<>();

        for(Cell cells : SAE.get().getBotKnowsCells()) {
            if(cells.getElement() == null ||
                    (!(cells.getElement() instanceof Mine) && !(cells.getElement() instanceof Storage)))
                continue;

            if(element.contains("storage") && cells.getElement() instanceof Storage && ((Storage) cells.getElement()).getOres() == bot.getOres())
                elementList.add(cells);
            if (element.contains("mine") && cells.getElement() instanceof Mine  && ((Mine) cells.getElement()).getOreLess() > 0 && ((Mine) cells.getElement()).getOres() == bot.getOres())
                elementList.add(cells);
        }

        if(elementList.isEmpty()) {
            System.out.println("[ERROR] Elements list is empty!");
            System.out.println("[WARN] Random moving to not block the element");
            Random random = new Random();
            boolean xory = random.nextBoolean();
            boolean m1orp1 = random.nextBoolean();
            Utils.actionMoving(bot, (xory ? "X" : "Y") + "/" + (m1orp1 ? "-" : "+"));
            return null;
        }

        for(Cell cell : elementList) {
            int distance = (int) Utils.calculateDistance(bot.getPosY(), bot.getPosX(), cell.getColumn(), cell.getRow());
            if(minDistance == -1 || distance < minDistance) {
                minDistance = distance;
                selected = cell;
            }
        }

        return selected;
    }
}
