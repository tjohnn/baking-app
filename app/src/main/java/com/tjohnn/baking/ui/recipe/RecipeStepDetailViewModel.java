package com.tjohnn.baking.ui.recipe;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.tjohnn.baking.data.dto.Ingredient;
import com.tjohnn.baking.data.dto.Recipe;
import com.tjohnn.baking.data.repository.RecipeRepository;
import com.tjohnn.baking.util.AppScheduler;
import com.tjohnn.baking.util.DataWrapper;
import com.tjohnn.baking.util.RxEventWrapper;
import com.tjohnn.baking.util.SimpleIdlingResource;

import io.reactivex.disposables.CompositeDisposable;

public class RecipeStepDetailViewModel extends AndroidViewModel {


    private final RecipeRepository recipeRepository;
    private final long recipeId;
    private final AppScheduler appScheduler;

    private MutableLiveData<Recipe> recipe = new MutableLiveData<>();
    private MutableLiveData<RxEventWrapper<String>> toaster = new MutableLiveData<>();
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public RecipeStepDetailViewModel(Application app, RecipeRepository recipeRepository, long recipeId, AppScheduler appScheduler) {
        super(app);
        this.recipeRepository = recipeRepository;
        this.recipeId = recipeId;

        this.appScheduler = appScheduler;
    }


    void loadRecipeById(){
        if(recipe.getValue() != null) return;
        System.out.println("Id is" + recipeId);
        SimpleIdlingResource.getInstance().setIdleState(false);
        compositeDisposable.add(recipeRepository.getRecipeById(recipeId)
                .subscribeOn(appScheduler.io())
                .observeOn(appScheduler.main())
                .subscribe(r -> {
                    this.recipe.setValue(r);
                    SimpleIdlingResource.getInstance().setIdleState(true);
                }, throwable -> {
                    toaster.setValue(new RxEventWrapper<>(throwable.getMessage()));
                    SimpleIdlingResource.getInstance().setIdleState(true);
                })
        );
    }

    public MutableLiveData<RxEventWrapper<String>> getToaster() {
        return toaster;
    }

    public LiveData<Recipe> getRecipe() {
        return recipe;
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }

    public static class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        private Application app;
        private final long recipeId;
        private RecipeRepository recipeRepository;
        private final AppScheduler scheduler;


        ViewModelFactory(Application app, RecipeRepository recipeRepository, long recipeId, AppScheduler scheduler) {
            this.app = app;
            this.recipeId = recipeId;
            this.recipeRepository = recipeRepository;
            this.scheduler = scheduler;
        }


        @NonNull
        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new RecipeStepDetailViewModel(app, recipeRepository, recipeId, scheduler);
        }
    }
}
