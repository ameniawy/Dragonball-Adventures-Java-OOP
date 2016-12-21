package dragonball.bonus;

import java.util.ArrayList;
import java.util.Random;

import dragonball.model.attack.Attack;
import dragonball.model.attack.MaximumCharge;
import dragonball.model.attack.PhysicalAttack;
import dragonball.model.attack.SuperAttack;
import dragonball.model.attack.SuperSaiyan;
import dragonball.model.attack.UltimateAttack;
import dragonball.model.battle.Battle;
import dragonball.model.battle.BattleOpponent;
import dragonball.model.character.fighter.Fighter;
import dragonball.model.character.fighter.Saiyan;
import dragonball.model.exceptions.NotEnoughKiException;

public class FoeBrain {
	private Fighter player;
	private Fighter foe;
	private Battle battle;
	
	private ArrayList<Attack> attacks;
	private ArrayList<Attack> possibleAttacks;
	
	private Attack bestAttack;
	private int bestAttackKi;
	
	private Attack bestPossibleAttack;
	private boolean hasMC;
	private BattleAction battleAction;
	int blockNum;
	
	public FoeBrain(Fighter me, Fighter foe, Battle battle) {
		this.blockNum = 0;
		
		this.player = me;
		this.foe = foe;
		this.battle = battle;
		
		//this.player.getUltimateAttacks().add(new SuperSaiyan());
		//this.player.setPhysicalDamage(50);
		
		this.attacks = battle.getAssignedAttacks();
		
		for(Attack a: this.attacks)
			System.out.println(a.getName());
		
		this.hasMC = false;
		for(Attack a:attacks)
			if(a.getName().equals("Maximum Charge"))
				this.hasMC = true;
		
		
		this.possibleAttacks = new ArrayList<Attack>();
		this.updatePossibleAttacks();
		
		int damage = 0;
		for(Attack a: attacks)
			if(a.getAppliedDamage((BattleOpponent)foe)>damage) {
				damage = a.getAppliedDamage((BattleOpponent)foe);
				this.bestAttack = a;
			}
		
		this.bestAttackKi = 0;
		
		if(this.bestAttack instanceof UltimateAttack)
			this.bestAttackKi = 3;
		if(this.bestAttack instanceof SuperAttack)
			this.bestAttackKi = 1;
		
		this.updateBestPossibleAttack();
		
		if(this.bestAttackKi==3) {
			int r = (new Random()).nextInt(100);
			
			if(r<30) {
				if(this.hasMC)
					r = (new Random()).nextInt(3);
				else
					r = (new Random()).nextInt(2);
				
				if(r==0)
					this.battleAction = BattleAction.BLOCK10;
				if(r==1)
					this.battleAction = BattleAction.BLOCK20;
				if(r==2)
					this.battleAction = BattleAction.BLOCK50;
			} else {
				if(this.hasMC)
					r = (new Random()).nextInt(3);
				else 
					r = (new Random()).nextInt(2);
				
				if(r==0)
					this.battleAction = BattleAction.P11;
				if(r==1)
					this.battleAction = BattleAction.P21;
				if(r==2)
					this.battleAction = BattleAction.MC51;
			}
		} else {
			int r = (new Random()).nextInt(100);
			
			if(r<50)
				this.battleAction = BattleAction.BLOCK60;
			else
				this.battleAction = BattleAction.P61;
		}
	}
	
	public void updatePossibleAttacks() {
		this.possibleAttacks.clear();
		for(Attack a: attacks) {
			//System.out.println(a.getName());
			if(a instanceof UltimateAttack && this.foe.getKi()>=3)
				this.possibleAttacks.add(a);
			if(a instanceof SuperAttack && this.foe.getKi()>=1 && !(a instanceof MaximumCharge))
				this.possibleAttacks.add(a);
		}
		this.possibleAttacks.add(new PhysicalAttack());
	}
	
	public void updateBestPossibleAttack() {
		int damage = 0;
		for(Attack a: possibleAttacks)
			if(a.getAppliedDamage((BattleOpponent)foe)>damage) {
				this.bestPossibleAttack = a;
				damage = a.getAppliedDamage((BattleOpponent)foe);
			}
		//System.out.println(this.bestPossibleAttack.getName());
	}
	
	public void thinkAndAttack() {
		this.updatePossibleAttacks();
		this.updateBestPossibleAttack();
		
		
		//this.player.setKi(5);
		//((Saiyan)(this.player)).setTransformed(true);
		
		if(this.willWin()) {
			this.battleAction = BattleAction.S72;
		} else {
			if(this.willLose() || this.playerTransformed()) {
				if(this.foe.getStamina()>0 && this.blockNum<3) {
					this.battleAction = BattleAction.PROTECTIVEBLOCK;
				} else {
					this.battleAction = BattleAction.S72;
				}
			} else {
				if(this.playerBlocking()) {
					if(this.bestAttackKi == 3) {
						if(this.hasMC) {
							if(this.foe.getKi() <2)
								this.battleAction = BattleAction.MC41;
							else
								this.battleAction = BattleAction.P31;
						} else {
							this.battleAction = BattleAction.P31;
						}	
					} else {
						this.battleAction = BattleAction.P71;
					}
				} else {
					if(this.foe.getKi()>=this.bestAttackKi) {
						if(this.bestAttackKi == 3) {
							if(this.battleAction != BattleAction.R52)
								this.battleAction = BattleAction.B15;
						} else {
							if(this.battleAction != BattleAction.R62) {
								this.battleAction = BattleAction.S63;
							}
						}
					}
				}
			}
		}
		
		System.out.println(this.battleAction.toString());
		
		
		
		switch(this.battleAction) {
		case B15:
		case B25:
		case B34:
		case B42:
		case B53:
			this.perfromBest();
			this.reroute();
			break;
		case BLOCK10:
			this.block();
			this.reroute(BattleAction.P11);
			break;
		case BLOCK20:
			this.block();
			this.reroute(BattleAction.P21);
			break;
		case BLOCK50:
			this.block();
			this.reroute(BattleAction.MC51);
			break;
		case BLOCK60:
			this.block();
			this.reroute(BattleAction.P61);
			break;
		case MC41:
			this.maximumCharge();
			this.reroute(BattleAction.B42);
			break;
		case MC51:
			this.maximumCharge();
			this.reroute(BattleAction.R52);
			break;
		case P11:
			this.physicalAttack();
			this.reroute(BattleAction.R12);
			break;
		case P13:
			this.physicalAttack();
			this.reroute(BattleAction.P14);
			break;
		case P14:
			this.physicalAttack();
			this.reroute(BattleAction.B15);
			break;
		case P21:
			this.physicalAttack();
			this.reroute(BattleAction.P22);
			break;
		case P22:
			this.physicalAttack();
			this.reroute(BattleAction.R23);
			break;
		case P24:
			this.physicalAttack();
			this.reroute(BattleAction.B25);
			break;
		case P31:
			this.physicalAttack();
			this.reroute(BattleAction.P32);
			break;
		case P32:
			this.physicalAttack();
			this.reroute(BattleAction.P33);
			break;
		case P33:
			this.physicalAttack();
			this.reroute(BattleAction.B34);
			break;
		case P61:
			this.physicalAttack();
			this.reroute(BattleAction.R62);
			break;
		case P71:
			this.physicalAttack();
			this.reroute(BattleAction.S72);
			break;
		case PROTECTIVEBLOCK:
			this.blockNum++;
			this.block();
			this.reroute();
			break;
		case R12:
			this.random();
			if(this.foe.getKi()==0)
				this.reroute(BattleAction.P31);
			else
				this.reroute(BattleAction.P13);
			break;
		case R23:
			this.random();
			if(this.foe.getKi()<2)
				this.reroute(BattleAction.P31);
			else
				this.reroute(BattleAction.P24);
			break;
		case R52:
			this.random();
			if(this.foe.getKi()<3)
				this.reroute(BattleAction.P31);
			else
				this.reroute(BattleAction.B53);
			break;
		case R62:
			this.random();
			this.reroute(BattleAction.S63);
			break;
		case S63:
			this.performStrongest();
			this.reroute();
			break;
		case S72:
			this.performStrongest();
			this.reroute();
			break;
		default:
			System.out.println("Dead End");
			this.battleAction = BattleAction.P71;
		}
	}
	
	public void performStrongest() {
		this.updatePossibleAttacks();
		this.updateBestPossibleAttack();
		
		int r = (new Random()).nextInt(3);
		
		if(r == 0)
			this.blockNum = 0;
		
		try {
			this.battle.attack(this.bestPossibleAttack);
		} catch (NotEnoughKiException e) {
			this.physicalAttack();
			System.out.println("Best possible attack was not possible, did a phyical attack instead");
		}
	}
	
	public void reroute(BattleAction a) {
		this.battleAction = a;
	}
	
	public void reroute() {
		if(this.bestAttackKi == 3) {
			int r = (new Random()).nextInt(3);
			
			if(r == 0)
				this.battleAction = BattleAction.P61;
			else {
				if(this.hasMC) {
					r = (new Random()).nextInt(2);
					
					if(r == 0)
						this.battleAction = BattleAction.P31;
					else
						this.battleAction = BattleAction.MC41;
				} else {
					this.battleAction = BattleAction.P31;
				}
			}
			
		} else {
			int r = (new Random()).nextInt(3);
			
			if(r == 0)
				this.battleAction = BattleAction.P61;
			else
				this.battleAction = BattleAction.P71;
		}
	}
	
	public void block() {
		this.battle.block();
	}
	
	public void physicalAttack() {
		//this.blockNum = 0;
		try {
			this.battle.attack(new PhysicalAttack());
		} catch (NotEnoughKiException e) {
			System.out.println("This will never be printed");
		}
	}
	
	public void perfromBest() {
		//this.blockNum = 0;
		try {
			this.battle.attack(bestAttack);
		} catch (NotEnoughKiException e) {
			this.physicalAttack();
			System.out.println("Did not have ennough ki to do the best attack, did a physical attack instead");
		}
	}
	
	public void random() {
		//this.blockNum = 0;
		int r = (new Random()).nextInt(100);
		
		if(r<40)
			this.battle.block();
		else {
			int i = (new Random()).nextInt(this.possibleAttacks.size());
			try {
				this.battle.attack(this.possibleAttacks.get(i));
			} catch (NotEnoughKiException e) {
				this.physicalAttack();
				System.out.println("Not all possible Attacks are possible, did a physical attack instead");
			}
		}
	}
	
	public void maximumCharge() {
		//this.blockNum = 0;
		if(this.hasMC)
			try {
				this.battle.attack(new MaximumCharge());
			} catch (NotEnoughKiException e) {
				this.physicalAttack();
				System.out.println("this will never get printed");
			}
		else {
			this.physicalAttack();
			System.out.println("Player didn't even have maximum charge, we did a physical attack instead");
		}
	}
	
	public boolean playerBlocking() {
		return this.battle.isMeBlocking();
	}
	
	public boolean playerTransformed() {
		return (this.player instanceof Saiyan)&&((Saiyan)this.player).isTransformed();
	}
	
	public boolean willWin() {
		this.updatePossibleAttacks();
		this.updateBestPossibleAttack();
		
		int bestDamage = this.bestPossibleAttack.getAppliedDamage((BattleOpponent)foe);
		int playerHealth = this.player.getHealthPoints();
		
		if(this.playerBlocking())
			playerHealth += this.player.getStamina()*100;
		
		return bestDamage>=playerHealth;
	}
	
	public boolean willLose() {
		int playerKi = this.player.getKi();
		int bestDamage = 0;
		
		if(playerKi>=3) {
			for(UltimateAttack a: this.player.getUltimateAttacks()) {
				if(a.getAppliedDamage((BattleOpponent)this.player)>bestDamage) {
					bestDamage = a.getAppliedDamage((BattleOpponent)this.player);
				}
			}
		}
		
		if(playerKi>=1) {
			for(SuperAttack a: this.player.getSuperAttacks()) {
				if(a.getAppliedDamage((BattleOpponent)this.player)>bestDamage) {
					bestDamage = a.getAppliedDamage((BattleOpponent)this.player);
				}
			}
		}
		
		if((new PhysicalAttack()).getAppliedDamage((BattleOpponent)this.player)>bestDamage) {
			bestDamage = (new PhysicalAttack()).getAppliedDamage((BattleOpponent)this.player);
		}
		
		int foeHealth = this.foe.getHealthPoints();
		
		return bestDamage>=foeHealth;
	}
}
