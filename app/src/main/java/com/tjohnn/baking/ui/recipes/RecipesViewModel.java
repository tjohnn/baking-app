package com.tjohnn.baking.ui.recipes;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.tjohnn.baking.data.dto.Ingredient;
import com.tjohnn.baking.data.dto.Recipe;
import com.tjohnn.baking.data.repository.RecipeRepository;
import com.tjohnn.baking.util.AppScheduler;
import com.tjohnn.baking.util.RxEventWrapper;
import com.tjohnn.baking.util.SimpleIdlingResource;

import java.util.List;
import java.util.Queue;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class RecipesViewModel extends AndroidViewModel {


    private final RecipeRepository recipeRepository;
    private final AppScheduler appScheduler;

    private MutableLiveData<List<Recipe>> recipes = new MutableLiveData<>();
    private MutableLiveData<RxEventWrapper<String>> toaster = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private RecipesViewModel(Application app, RecipeRepository recipeRepository, AppScheduler appScheduler) {
        super(app);
        this.recipeRepository = recipeRepository;
        this.appScheduler = appScheduler;


    }


    void loadRecipes(){
        SimpleIdlingResource.getInstance().setIdleState(false);
        compositeDisposable.add(recipeRepository.getRecipes()
                .subscribeOn(appScheduler.io())
                .observeOn(appScheduler.main())
                .subscribe(r -> {
                    recipes.setValue(r);
                    SimpleIdlingResource.getInstance().setIdleState(true);
                }, throwable -> {
                    toaster.setValue(new RxEventWrapper<>(throwable.getMessage()));
                    SimpleIdlingResource.getInstance().setIdleState(true);
                })
        );
    }

    MutableLiveData<RxEventWrapper<String>> getToaster() {
        return toaster;
    }

    public MutableLiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }

    public static class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        private Application app;
        private RecipeRepository recipeRepository;
        private final AppScheduler scheduler;


        ViewModelFactory(Application app, RecipeRepository recipeRepository, AppScheduler scheduler) {
            this.app = app;
            this.recipeRepository = recipeRepository;
            this.scheduler = scheduler;
        }


        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new RecipesViewModel(app, recipeRepository, scheduler);
        }
    }


}
