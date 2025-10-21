# Migration to Minecraft 1.21.10 - Status Report

## ✅ Completed Tasks

1. **Updated Dependencies** (`gradle.properties`):
   - yarn_mappings: `1.21.10+build.1` → `1.21.10+build.2`
   - fabric_version: `0.134.1+1.21.10` → `0.136.0+1.21.10`

2. **Ran Mapping Migration**:
   - Executed: `gradlew migrateMappings --mappings "1.21.10+build.2"`
   - All source files remapped successfully to new yarn mappings
   - Remapped sources copied from `remappedSrc/` to `src/`

3. **Updated `fabric.mod.json`**:
   - Changed minecraft dependency from `>=1.21` to `~1.21.10`

4. **Fixed Initial Code-Breaking Changes**:
   - ✅ `LTNTBlock.java`: Fixed `PiglinBrain.onGuardedBlockInteracted()` to require `ServerWorld` parameter
   - ✅ `LTNTBlock.java`: Fixed `EntityType.create()` to use `SpawnReason.TRIGGERED` parameter
   - ✅ `LTNTBlock.java`: Fixed `ProjectileEntity.canModifyAt()` to require `ServerWorld` parameter  
   - ✅ `LTNTBlock.java`: Changed `world.isClient` to `world.isClient()`
   - ✅ `LTNTBlockEntity.java`: Changed `writeNbt()/readNbt()` visibility to `protected`
   - ✅ `LTNTBlockEntity.java`: Fixed `getCompound()` returning `Optional<NbtCompound>`

## ⚠️ Known Issues Requiring Manual Fix

### 1. **ImprovedExplosion.java** - MAJOR REFACTORING NEEDED
**Issue**: In Minecraft 1.21.10, the `Explosion` class is now a final record class and cannot be extended.

**Current State**: `public class ImprovedExplosion extends Explosion` - THIS WILL NOT COMPILE

**Required Changes**:
- Change from inheritance to composition pattern
- Store an `Explosion` instance as a field instead of extending it
- Add wrapper methods for all Explosion functionality
- Update all methods that pass `this` to expect `Explosion` instead of `ImprovedExplosion`
- Fix methods like:
  - `damageCalculator.getBlastResistance(this, ...)` 
  - `damageCalculator.canDestroyBlock(this, ...)`
  - `entity.isImmuneToExplosion(this)`
  - `block.onDestroyedByExplosion(level, pos, this)`

**Affected Files**:
- `src/main/java/luckytntlib/util/explosions/ImprovedExplosion.java`
- Any TNT effects that use ImprovedExplosion

### 2. **LTNTMinecart.java** - Minecart Type Removed
**Issue**: `AbstractMinecartEntity.Type` enum appears to have been removed or changed in 1.21.10

**Current State**: Method `getMinecartType()` is commented out

**Required Changes**:
- Verify if `getMinecartType()` method still exists in `AbstractMinecartEntity`
- If removed, delete the method entirely
- If changed, update to new signature/return type
- Check Minecraft 1.21.10 source or Fabric API docs for correct implementation

### 3. **Client-Side Code** - Package Import Errors
**Issue**: Client-only classes cannot be imported in main source set (split source sets are enabled)

**Affected Classes**:
- `ClientAccess.java` - imports `net.minecraft.client.MinecraftClient`, `Screen`
- `ConfigScreen.java` - all client GUI classes
- `ConfigScreenListScreen.java` - client GUI classes
- `AdvancedSlider.java`, `CenteredStringWidget.java` - client widgets  
- Renderers: `LDynamiteRenderer.java`, `LTNTRenderer.java`, `LTNTMinecartRenderer.java`
- `ClientNetworkRegistry.java`, `EventRegistry.java` - client networking
- `RegistryHelper.java` - has client networking imports

**Current State**: These files are in `src/main/java` but use client-only imports

**Options**:
1. Move affected files to `src/client/java` sourceset
2. OR disable split source sets in `build.gradle` (remove `splitEnvironmentSourceSets()`)
3. OR create proper client/main separation

### 4. **API Changes Requiring Verification**
The following API changes need manual verification against Minecraft 1.21.10 source:

- ✅ `BlockEntity.writeNbt()` / `readNbt()` - Fixed to use `protected` and handle `Optional`
- ❓ `LootContextParameterSet` - Class may have moved or been renamed
- ❓ `ItemActionResult` - Class exists in remapped source but shows errors
- ❓ `TypedActionResult` - May have been replaced with `ActionResult`
- ❓ `Block.neighborUpdate()` - Method signature may have changed
- ❓ `Block.onDestroyedByExplosion()` - Now requires `ServerWorld` instead of `World`
- ❓ `BlockState.isOpaqueFullCube()` - Method signature changed (no longer takes parameters)

### 5. **TypedActionResult → ActionResult Changes**
**Affected Files**:
- `LDynamiteItem.java`: `use()` method returns `TypedActionResult<ItemStack>`
- `TNTConfigItem.java`: `use()` method returns `TypedActionResult<ItemStack>`

**Required Changes**:
- Check if `TypedActionResult` still exists or has been replaced with `ActionResult`
- Update method signatures and return statements accordingly

## 📋 Next Steps

1. **Priority 1 - Fix ImprovedExplosion**:
   - This is the core explosion system used throughout the mod
   - Requires complete refactoring from inheritance to composition
   - Estimated effort: 2-4 hours

2. **Priority 2 - Fix Client Source Sets**:
   - Either move client files or disable split source sets
   - Test that client rendering still works
   - Estimated effort: 1-2 hours

3. **Priority 3 - Verify Remaining API Changes**:
   - Check each `❓` item against Minecraft 1.21.10 source
   - Update method signatures and fix compilation errors
   - Estimated effort: 1-2 hours

4. **Priority 4 - Test Build**:
   - Run `gradlew build` to check for remaining errors
   - Fix any remaining compilation issues
   - Run in-game tests

## 🔧 Commands for Testing

```powershell
# Clean and rebuild
.\gradlew clean build

# Run client to test in-game
.\gradlew runClient

# Generate sources for reference
.\gradlew genSources
```

## 📚 References

- Fabric API 0.136.0+1.21.10 Docs: https://maven.fabricmc.net/docs/fabric-api-0.136.0+1.21.10/
- Yarn Mappings 1.21.10+build.2: https://fabricmc.net/develop/
- Minecraft 1.21.10 Changes: Check Fabric Discord or changelog

## ⚡ Quick Fix to Get Building

If you need the project to compile quickly (even with limited functionality):

1. Comment out the entire `ImprovedExplosion` class body
2. Disable split source sets in `build.gradle` by commenting out `splitEnvironmentSourceSets()`
3. Run `.\gradlew build --continue` to see all remaining errors

---

**Date**: October 20, 2025  
**Migration Status**: ~60% Complete  
**Blocking Issues**: ImprovedExplosion refactoring, Client source set separation  
**Estimated Time to Complete**: 4-8 hours
